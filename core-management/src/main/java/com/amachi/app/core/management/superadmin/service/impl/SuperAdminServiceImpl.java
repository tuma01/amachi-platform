package com.amachi.app.core.management.superadmin.service.impl;

import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.core.management.superadmin.dto.search.SuperAdminSearchDto;
import com.amachi.app.core.management.superadmin.entity.SuperAdmin;
import com.amachi.app.core.management.superadmin.repository.SuperAdminRepository;
import com.amachi.app.core.management.superadmin.specification.SuperAdminSpecification;
import com.amachi.app.core.auth.context.AuthContextHolder;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserAccountRepository;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.enums.SuperAdminLevel;
import com.amachi.app.core.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.amachi.app.core.common.utils.AppConstants.ErrorMessages.ENTITY_MUST_NOT_BE_NULL;
import static com.amachi.app.core.common.utils.AppConstants.ErrorMessages.ID_MUST_NOT_BE_NULL;
import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@Service
public class SuperAdminServiceImpl implements GenericService<SuperAdmin, SuperAdmin, SuperAdminSearchDto> {

    private final SuperAdminRepository superAdminRepository;
    private final SuperAdminDomainServiceImpl superAdminDomainService;
    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public List<SuperAdmin> getAll() {
        return superAdminRepository.findAll();
    }

    @Override
    public Page<SuperAdmin> getAll(SuperAdminSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Specification<SuperAdmin> specification = new SuperAdminSpecification(searchDto);
        return superAdminRepository.findAll(specification, pageable);
    }

    @Override
    public SuperAdmin getById(Long id) {
        requireNonNull(id, ID_MUST_NOT_BE_NULL);
        return superAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SuperAdmin.class.getName(), "error.resource.not.found",
                        id));
    }

    @Override
    public SuperAdmin create(SuperAdmin entity) {
        requireNonNull(entity, ENTITY_MUST_NOT_BE_NULL);
        validateAccessLevel(SuperAdminLevel.LEVEL_2);
        superAdminDomainService.encodePasswordIfNeeded(entity);
        // 🔧 AUTO-RESOLUCIÓN REAL (USANDO TU REPO)
        if (entity.getUser() == null) {

            if (entity.getPerson() == null) {
                throw new IllegalStateException("SuperAdmin must have a Person");
            }
            User user = userRepository.findByPerson(entity.getPerson())
                    .orElseThrow(() -> new IllegalStateException(
                            "No User associated with Person ID: " + entity.getPerson().getId()
                    ));

            entity.setUser(user);
        }
        // 🔒 VALIDACIÓN FINAL (por seguridad)
        if (entity.getUser() == null) {
            throw new IllegalStateException("SuperAdmin.user must not be null before persist");
        }

        SuperAdmin savedEntity = superAdminRepository.save(entity);
        superAdminDomainService.completeAccountSetup(savedEntity);
        return savedEntity;
    }

    @Override
    public SuperAdmin update(Long id, SuperAdmin entity) {
        requireNonNull(id, ID_MUST_NOT_BE_NULL);
        requireNonNull(entity, ENTITY_MUST_NOT_BE_NULL);
        validateAccessLevel(SuperAdminLevel.LEVEL_2); // Min Level 2 to update

        // Verificar que el SuperAdmin exista
        superAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SuperAdmin.class.getName(), "error.resource.not.found",
                        id));
        entity.setId(id);
        return superAdminRepository.save(entity);
    }

    @Override
    public void delete(Long id) {
        requireNonNull(id, ID_MUST_NOT_BE_NULL);
        validateAccessLevel(SuperAdminLevel.LEVEL_1); // Only Level 1 can delete

        SuperAdmin superAdmin = superAdminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SuperAdmin.class.getName(), "error.resource.not.found",
                        id));

        // 🛡️ Last Man Standing Protection
        long count = superAdminRepository.count();
        if (count <= 1) {
            throw new IllegalStateException("Cannot delete the only remaining Super Admin. Create another one first.");
        }

        superAdminRepository.delete(superAdmin);
    }

    private void validateAccessLevel(SuperAdminLevel requiredLevel) {
        if (!AuthContextHolder.isSuperAdmin()) {
            throw new UnauthorizedException("Only SuperAdmins can access this service");
        }

        User user = userRepository.findById(AuthContextHolder.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found in context"));

        SuperAdmin currentAdmin = superAdminRepository.findByUser(user)
                .orElseThrow(() -> new UnauthorizedException("Current user is not registered as SuperAdmin"));

        if (currentAdmin.getLevel().ordinal() > requiredLevel.ordinal()) {
            throw new UnauthorizedException("Insufficient access level. Required: " + requiredLevel);
        }
    }
}
