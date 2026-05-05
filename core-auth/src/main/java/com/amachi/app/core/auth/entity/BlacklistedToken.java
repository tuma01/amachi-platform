package com.amachi.app.core.auth.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Token JWT invalidado tras logout o revocación explícita.
 * Multi-tenant scoped (Elite Tier): el blacklist de un tenant
 * no interfiere con los tokens de otro tenant.
 */
@Entity
@Table(
    name = "AUT_BLACKLISTED_TOKEN",
    indexes = {
        @Index(name = "IDX_BLACKLISTED_TOKEN_TOKEN",  columnList = "TOKEN"),
        @Index(name = "IDX_BLACKLISTED_TOKEN_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_BLACKLISTED_TOKEN_EXPIRY", columnList = "EXPIRES_AT")
    }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken extends AuditableTenantEntity {

    @Column(name = "TOKEN", nullable = false, unique = true, length = 500)
    private String token;

    @Column(name = "BLACKLISTED_AT", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;
}
