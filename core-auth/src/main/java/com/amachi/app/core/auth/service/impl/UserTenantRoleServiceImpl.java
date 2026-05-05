package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.dto.UserTenantRoleDto;
import com.amachi.app.core.auth.dto.search.UserTenantRoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.auth.mapper.UserTenantRoleMapper;
import com.amachi.app.core.auth.repository.RoleRepository;
import com.amachi.app.core.auth.repository.UserTenantRoleRepository;
import com.amachi.app.core.auth.service.UserTenantRoleService;
import com.amachi.app.core.auth.specification.UserTenantRoleSpecification;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@TenantAware
@RequiredArgsConstructor
@Slf4j
public class UserTenantRoleServiceImpl extends BaseService<UserTenantRole, UserTenantRoleDto, UserTenantRoleSearchDto> 
        implements UserTenantRoleService {

    private final RoleRepository roleRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final TenantRepository tenantRepository;
    private final DomainEventPublisher eventPublisher;
    private final UserTenantRoleMapper mapper;

    @PersistenceContext
    private final EntityManager em;

    @Override
    protected CommonRepository<UserTenantRole, Long> getRepository() {
        return userTenantRoleRepository;
    }

    @Override
    protected Specification<UserTenantRole> buildSpecification(UserTenantRoleSearchDto searchDto) {
        return new UserTenantRoleSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected UserTenantRole mapToEntity(UserTenantRoleDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(UserTenantRoleDto dto, UserTenantRole existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(UserTenantRole entity) {
        // Publíquense eventos de privilegio si es necesario
    }

    @Override
    protected void publishUpdatedEvent(UserTenantRole entity) {
        // Publíquense eventos de revocación o cambio si es necesario
    }

    @Transactional
    public void assignRolesToUserAndTenant(Long userId, Long tenantId, List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return;

        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) return;

        User userRef = em.getReference(User.class, userId);
        Tenant tenantRef = em.getReference(Tenant.class, tenantId);

        List<UserTenantRole> entities = roles.stream()
                .map(role -> UserTenantRole.builder()
                        .user(userRef)
                        .tenantId(tenantId)
                        .role(role)
                        .active(true)
                        .assignedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        userTenantRoleRepository.saveAll(entities);
    }

    @Transactional
    public Set<UserTenantRole> assignRolesToUserAndTenant(User user, Long tenantId, Set<Role> roles) {

        if (roles == null || roles.isEmpty()) return Set.of();

        User userRef = em.getReference(User.class, user.getId());

        Set<UserTenantRole> toPersist = new HashSet<>();

        for (Role role : roles) {
            if (!userTenantRoleRepository
                    .existsByUserAndTenantIdAndRoleAndDeletedFalse(userRef, tenantId, role)) {

                toPersist.add(UserTenantRole.builder()
                        .user(userRef)
                        .tenantId(tenantId)
                        .role(role)
                        .active(true)
                        .assignedAt(LocalDateTime.now())
                        .build());
            }
        }

        if (!toPersist.isEmpty()) {
            return new HashSet<>(userTenantRoleRepository.saveAll(toPersist));
        }

        return Set.of();
    }

    public Set<String> findRoleNamesByUserAndTenant(Long userId, Long tenantId) {
        if (userId == null || tenantId == null) return Set.of();
        return new HashSet<>(userTenantRoleRepository.findActiveRoleNamesByUserAndTenant(userId, tenantId));
    }

    @Transactional
    public void unassignRolesFromUserAndTenant(Long userId, Long tenantId, List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return;
        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if (roles.isEmpty()) return;

        User userRef = em.getReference(User.class, userId);
        Tenant tenantRef = em.getReference(Tenant.class, tenantId);

        List<UserTenantRole> toDelete = userTenantRoleRepository.findByUserAndTenantAndRoleIn(userRef, tenantRef, roles);
        if (!toDelete.isEmpty()) {
            userTenantRoleRepository.deleteAll(toDelete);
        }
    }

    @Transactional
    @Override
    public void revokeRole(Long userId, Long tenantId, String roleName) {
        if (roleName == null || roleName.isBlank()) return;
        
        User userRef = em.getReference(User.class, userId);
        Tenant tenantRef = em.getReference(Tenant.class, tenantId);
        
        List<UserTenantRole> matches = userTenantRoleRepository.findAll((root, query, cb) -> cb.and(
                cb.equal(root.get("user"), userRef),
                cb.equal(root.get("tenant"), tenantRef),
                cb.equal(root.get("role").get("name"), roleName),
                cb.isTrue(root.get("active")),
                cb.isNull(root.get("revokedAt"))));

        for (UserTenantRole utr : matches) {
            utr.setActive(false);
            utr.setRevokedAt(LocalDateTime.now());
            userTenantRoleRepository.save(utr);
        }
    }
}
