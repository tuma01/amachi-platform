package com.amachi.app.core.common.constants;

/**
 * Technical and System-wide constants for core modules.
 * SaaS Elite Standard: No business logic, only technical markers and defaults.
 */
public final class SystemConstants {

    private SystemConstants() {
        throw new AssertionError("Constants class cannot be instantiated");
    }

    public static final String DEFAULT_TENANT_CODE = "SYSTEM";
    public static final Long DEFAULT_TENANT_ID = 0L;
    
    public static final String SYSTEM_USER = "SYSTEM";
    
    public static final class Security {
        public static final String ROLE_PREFIX = "ROLE_";
        public static final String AUTH_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String TENANT_HEADER = "X-Tenant-ID"; // Standard header name
    }

    public static final class Cache {
        public static final String TENANT_CACHE = "tenants";
        public static final String USER_CACHE = "users";
    }
}
