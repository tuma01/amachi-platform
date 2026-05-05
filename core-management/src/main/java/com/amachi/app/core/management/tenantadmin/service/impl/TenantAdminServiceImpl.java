package com.amachi.app.core.management.tenantadmin.service.impl;

import com.amachi.app.core.auth.context.AuthContextHolder;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.enums.TenantAdminLevel;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.core.management.tenantadmin.dto.search.TenantAdminSearchDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import com.amachi.app.core.management.tenantadmin.event.TenantAdminCreatedEvent;
import com.amachi.app.core.management.tenantadmin.repository.TenantAdminRepository;
import com.amachi.app.core.management.tenantadmin.service.TenantAdminService;
import com.amachi.app.core.management.tenantadmin.specification.TenantAdminSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Slf4j
public class TenantAdminServiceImpl extends BaseService<TenantAdmin, TenantAdmin, TenantAdminSearchDto> implements TenantAdminService {

    private final TenantAdminRepository repository;
    private final TenantAdminDomainServiceImpl tenantAdminDomainService;
    private final TenantRepository tenantRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<TenantAdmin, Long> getRepository() {
        return repository;
    }

    @Override
    public TenantAdmin getById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("TenantAdmin", "error.resource.not.found", null);
        }
        
        // 🛡️ Security Check: Use findByIdAndTenantId if not SuperAdmin
        if (AuthContextHolder.isSuperAdmin()) {
            return repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TenantAdmin", "error.resource.not.found", id));
        }

        return repository.findByIdAndTenantId(id, AuthContextHolder.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("TenantAdmin", "error.resource.not.found", id));
    }

    @Override
    protected Specification<TenantAdmin> buildSpecification(TenantAdminSearchDto searchDto) {
        return new TenantAdminSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected TenantAdmin mapToEntity(TenantAdmin entity) {
        return entity;
    }

    @Override
    protected void mergeEntities(TenantAdmin dto, TenantAdmin existing) {
        // En este servicio, la mezcla se realiza externamente o vía domain service
    }

    @Override
    protected void publishCreatedEvent(TenantAdmin entity) {
        eventPublisher.publish(new TenantAdminCreatedEvent(
                entity.getId(),
                entity.getUser() != null ? entity.getUser().getEmail() : null,
                entity.getTenant() != null ? entity.getTenant().getCode() : null,
                entity.getAdminLevel()
        ));
    }

    @Override
    protected void publishUpdatedEvent(TenantAdmin entity) {
        // Evento de actualización si aplica
    }

    @Override
    @Transactional
    public TenantAdmin create(TenantAdmin entity) {
        if (entity == null) {
            throw new ResourceNotFoundException("TenantAdmin", "error.entity.null", null);
        }

        // 🛡️ REFUERZO DE TENANT (Punto 4 Auditoría)
        if (!AuthContextHolder.isSuperAdmin()) {
            entity.setTenantId(AuthContextHolder.getTenantId());
            entity.setTenantCode(AuthContextHolder.getTenantCode());
        }

        // Handle existing Tenant attachment
        if (entity.getTenant() != null && entity.getTenant().getId() != null) {
            Tenant existingTenant = tenantRepository.findById(entity.getTenant().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tenant", "error.resource.not.found", entity.getTenant().getId()));

            entity.setTenant(existingTenant);

            // 🛡️ Business Rule: First admin is LEVEL_1
            long adminCount = repository.countByTenantIdAndUserEnabledTrue(existingTenant.getId());
            if (adminCount == 0) {
                entity.setAdminLevel(TenantAdminLevel.LEVEL_1);
            }
        }

        TenantAdmin savedEntity = super.create(entity);
        tenantAdminDomainService.completeAccountSetup(savedEntity);
        return savedEntity;
    }

    @Override
    @Transactional
    public TenantAdmin update(Long id, TenantAdmin entity) {
        TenantAdmin existingAdmin = getById(id);

        // 🛡️ Protection: Cannot downgrade the last LEVEL_1 admin
        if (existingAdmin.getAdminLevel() == TenantAdminLevel.LEVEL_1 && entity.getAdminLevel() != TenantAdminLevel.LEVEL_1) {
            long level1Count = repository.countByTenantIdAndAdminLevelAndUserEnabledTrue(existingAdmin.getTenant().getId(), TenantAdminLevel.LEVEL_1);
            if (level1Count <= 1) {
                throw new IllegalStateException("Cannot downgrade the last LEVEL_1 administrator.");
            }
        }

        entity.setId(id);
        return super.update(id, entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TenantAdmin tenantAdmin = getById(id);

        // 🛡️ Last Man Standing Protection
        if (tenantAdmin.getAdminLevel() == TenantAdminLevel.LEVEL_1) {
            long level1Count = repository.countByTenantIdAndAdminLevelAndUserEnabledTrue(tenantAdmin.getTenant().getId(), TenantAdminLevel.LEVEL_1);
            if (level1Count <= 1) {
                throw new IllegalStateException("Cannot delete the last LEVEL_1 administrator.");
            }
        }

        if (tenantAdmin.getUser() != null) {
            tenantAdmin.getUser().setEnabled(false);
        }
        repository.save(tenantAdmin);
    }
}
