package com.amachi.app.core.management.tenantconfig.repository;

import com.amachi.app.core.management.tenantconfig.entity.TenantConfig;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantConfigRepository extends TenantCommonRepository<TenantConfig, Long> {
    Optional<TenantConfig> findByTenantCode(String tenantCode);
    Optional<TenantConfig> findByTenantId(Long tenantId);
    boolean existsByTenantId(Long tenantId);
}
