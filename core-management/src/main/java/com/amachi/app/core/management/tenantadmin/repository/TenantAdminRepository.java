package com.amachi.app.core.management.tenantadmin.repository;

import com.amachi.app.core.common.enums.TenantAdminLevel;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantAdminRepository extends TenantCommonRepository<TenantAdmin, Long> {
    long countByTenantIdAndUserEnabledTrue(Long tenantId);
    long countByTenantIdAndAdminLevelAndUserEnabledTrue(Long tenantId, TenantAdminLevel level);
    
    boolean existsByPersonAndTenant(Person person, Tenant tenant);
    Optional<TenantAdmin> findByPersonAndTenant(Person person, Tenant tenant);
}
