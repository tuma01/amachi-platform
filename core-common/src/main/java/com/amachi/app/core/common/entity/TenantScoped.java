package com.amachi.app.core.common.entity;

/**
 * Contrato Técnico SaaS - Amachi Platform Docs.
 * SaaS Elite Tier: Distinguishing between numeric ID and alphanumeric Code.
 */
public interface TenantScoped {
    String getTenantCode();
    void setTenantCode(String tenantCode);

    Long getTenantId();
    void setTenantId(Long tenantId);
}
