package com.amachi.app.core.management.tenantadmin.service;

import com.amachi.app.core.management.tenantadmin.dto.TenantAdminDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;

public interface TenantAdminDomainService {

    TenantAdminDto enrichTenantAdminDto(TenantAdmin entity, TenantAdminDto dto);

    void handleTenantAddress(TenantAdmin entity, TenantAdminDto dto);

    void encodePasswordIfNeeded(TenantAdmin entity);

    void encodePasswordIfPresent(TenantAdmin entity, TenantAdminDto dto);

    void resetAdminPassword(TenantAdmin entity, String password);

    void completeAccountSetup(TenantAdmin savedEntity);
}
