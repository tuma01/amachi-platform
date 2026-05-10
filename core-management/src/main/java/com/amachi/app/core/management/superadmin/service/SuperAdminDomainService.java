package com.amachi.app.core.management.superadmin.service;

import com.amachi.app.core.management.superadmin.entity.SuperAdmin;

public interface SuperAdminDomainService {

    void encodePasswordIfNeeded(SuperAdmin entity);

    void completeAccountSetup(SuperAdmin savedEntity);
}
