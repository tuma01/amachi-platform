package com.amachi.app.core.auth.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad para tokens de activación de cuenta (onboarding).
 * Posee aislamiento nativo multi-tenant (Elite Tier).
 */
@Entity
@Getter
@SuperBuilder
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "AUT_ACTIVATION_TOKEN",
    uniqueConstraints = @UniqueConstraint(name = "UK_ACTIVATION_TOKEN", columnNames = {"TOKEN"}),
    indexes = @Index(name = "IDX_ACTIVATION_TENANT", columnList = "TENANT_ID")
)
public class ActivationToken extends AuditableTenantEntity {

    @Column(name = "TOKEN", nullable = false, unique = true, length = 100)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "FK_ID_USER", 
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_ACTIVATION_USER")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "TENANT_ID",
        nullable = false,
        insertable = false,
        updatable = false,
        foreignKey = @ForeignKey(name = "FK_ACTIVATION_TENANT")
    )
    private Tenant tenant;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;
    @Builder.Default
    @Column(name = "USED", nullable = false)
    private boolean used = false;

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }
}
