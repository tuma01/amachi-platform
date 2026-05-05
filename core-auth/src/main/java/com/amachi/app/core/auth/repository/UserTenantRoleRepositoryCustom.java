package com.amachi.app.core.auth.repository;

import java.util.Set;

/**
 * Custom repository interface for complex User-Tenant-Role operations.
 */
public interface UserTenantRoleRepositoryCustom {

    /**
     * Assigns a set of roles to a user within a specific tenant context.
     * 
     * @param userId     The ID of the user.
     * @param tenantCode The code of the tenant.
     * @param roleNames  A set of role names to assign.
     */
    void assignRolesToUserAndTenant(Long userId, String tenantCode, Set<String> roleNames);
}
