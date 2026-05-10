package com.amachi.app.core.management.tenant.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.domain.tenant.dto.search.TenantSearchDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;

public interface TenantService extends GenericService<Tenant, Tenant, TenantSearchDto> {

    Tenant getByCode(String code);

    Tenant createWithDetails(Tenant entity, TenantDto dto);

    Tenant updateWithDetails(Long id, Tenant entity, TenantDto dto);

    void enableTenant(Long id);

    void disableTenant(Long id);
}
