package com.amachi.app.core.auth.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

/**
 * Token de recuperación de contraseña.
 * Multi-tenant scoped (Elite Tier): un token de reset de tenant A
 * no puede ser utilizado en tenant B.
 */
@Entity
@Table(
    name = "AUT_PASSWORD_RESET_TOKEN",
    indexes = {
        @Index(name = "IDX_PASSWORD_RESET_TOKEN_TOKEN",  columnList = "TOKEN"),
        @Index(name = "IDX_PASSWORD_RESET_TOKEN_USER",   columnList = "FK_ID_USER"),
        @Index(name = "IDX_PASSWORD_RESET_TOKEN_TENANT", columnList = "TENANT_ID")
    }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken extends AuditableTenantEntity {

    @Column(name = "TOKEN", nullable = false, unique = true, length = 255)
    private String token;

    @Column(name = "EXPIRATION_DATE", nullable = false)
    private Instant expirationDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_RESET_TOKEN_USER"))
    private User user;

    @Builder.Default
    @Column(name = "USED", nullable = false)
    private boolean used = false;

    public boolean isExpired() {
        return expirationDate.isBefore(Instant.now());
    }

    public boolean isInvalid() {
        return used || isExpired();
    }
}
