package com.amachi.app.core.auth.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AUT_USER_TENANT_ROLE", uniqueConstraints = {
        @UniqueConstraint(
                name = "UK_USER_TENANT_ROLE",
                columnNames = { "FK_ID_USER", "TENANT_ID", "FK_ID_ROLE", "IS_DELETED" })
})
public class UserTenantRole extends AuditableTenantEntity implements Model {

    // ==========================================
    // Relationships
    // ==========================================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_TENANT_ROLE_USER"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "TENANT_ID",
            nullable = false,
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "FK_USER_TENANT_ROLE_TENANT")
    )
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_ROLE", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_TENANT_ROLE_ROLE"))
    private Role role;

    // ==========================================
    // Business fields
    // ==========================================
    /**
     * Metadata opcional: fecha en que se asignó el rol.
     * Mapeado al createdDate de Auditable.
     */
    @Column(name = "ASSIGNED_AT", nullable = false, updatable = false)
    private LocalDateTime assignedAt;

    /**
     * Metadata opcional: fecha en que se revocó el rol.
     */
    @Column(name = "REVOKED_AT")
    private LocalDateTime revokedAt;

    /**
     * Estado del rol (activo/inactivo).
     */
    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    // ==========================================
    // Lifecycle
    // ==========================================
    @PrePersist
    public void prePersist() {
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }
}
