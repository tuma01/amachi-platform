package com.amachi.app.core.domain.entity;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.entity.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
@org.hibernate.annotations.FilterDef(name = "tenantFilter", parameters = @org.hibernate.annotations.ParamDef(name = "tenantId", type = Long.class))
@org.hibernate.annotations.Filter(name = "tenantFilter", condition = "TENANT_ID = :tenantId")
@org.hibernate.annotations.SQLRestriction("IS_DELETED = false")
public abstract class AuditableTenantEntity extends Auditable<String> implements SoftDeletable, Model, TenantScoped {

    // ==========================================
    // Tenant Isolation (Hardened Standard)
    // ==========================================
    @Column(name = "TENANT_ID", nullable = false, updatable = false)
    private Long tenantId;

    @Column(name = "TENANT_CODE", length = 50, updatable = false)
    private String tenantCode;

    // ==========================================
    // Soft Delete
    // ==========================================
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean deleted = false;

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    // ==========================================
    // Lifecycle (Dumb & Consistent)
    // ==========================================
    @PrePersist
    public void onPrePersist() {
        // 1. Asignación Directa desde Contexto (Runtime)
        if (this.tenantId == null) {
            this.tenantId = TenantContext.getTenantId();
        }

        if (this.tenantCode == null) {
                    this.tenantCode = TenantContext.getTenantCode();
        }

        // 2. Validación de Integridad (Evita corrupción silenciosa)
        if (this.tenantId == null || this.tenantCode == null) {
            throw new IllegalStateException("Multi-tenant isolation fields (ID/CODE) are missing for entity: " 
                + this.getClass().getSimpleName() + ". Context must be set or fields explicitly assigned.");
        }

        if (this.deleted == null) {
            this.deleted = false;
        }
    }
}
