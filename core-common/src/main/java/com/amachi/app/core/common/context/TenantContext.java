package com.amachi.app.core.common.context;

/**
 * Contexto de ejecución para el Tenant actual (SaaS Elite Simple).
 * Mantiene el aislamiento de datos en tiempo de ejecución.
 */
public final class TenantContext {

    private static final ThreadLocal<Long> CURRENT_TENANT = new ThreadLocal<>();
    private static final ThreadLocal<String> CURRENT_TENANT_CODE = new ThreadLocal<>();

    private TenantContext() {}

    /**
     * Establece el ID del tenant
     */
    public static void setTenantId(Long tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    /**
     * Obtiene el ID del tenant actual
     */
    public static Long getTenantId() {
        return CURRENT_TENANT.get();
    }

    /**
     * Establece el CÓDIGO del tenant
     */
    public static void setTenantCode(String tenantCode) {
        CURRENT_TENANT_CODE.set(tenantCode);
    }

    /**
     * Obtiene el código del tenant actual
     */
    public static String getTenantCode() {
        return CURRENT_TENANT_CODE.get();
    }

    /**
     * Limpia el contexto (IMPORTANTE en filtros)
     */
    public static void clear() {
        CURRENT_TENANT.remove();
        CURRENT_TENANT_CODE.remove();
    }
}
