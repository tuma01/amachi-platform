package com.amachi.app.core.management.tenant.service;

import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;

public interface TenantDomainService {

    void handleTenantAddress(Tenant entity, TenantDto dto);

    void handleTenantTheme(Tenant entity, TenantDto dto);

    TenantDto enrichTenantDto(Tenant entity, TenantDto dto);
}
