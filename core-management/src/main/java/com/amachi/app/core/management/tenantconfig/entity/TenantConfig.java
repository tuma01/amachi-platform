package com.amachi.app.core.management.tenantconfig.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.common.enums.SubscriptionPlan;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.*;


import java.time.LocalDate;

@Entity
@Table(name = "MGT_TENANT_CONFIG")
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TenantConfig extends AuditableTenantEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_TENANT_CONFIG_TENANT"))
    private Tenant tenant;

    @Column(name = "FALLBACK_HEADER", length = 100)
    private String fallbackHeader;

    @Column(name = "ALLOW_LOCAL")
    private Boolean allowLocal;

    @Column(name = "DEFAULT_DOMAIN", length = 200)
    private String defaultDomain;

    @Column(name = "LOCALE", length = 20)
    private String locale;

    @Column(name = "TIMEZONE", length = 100)
    private String timezone;

    @Column(name = "MAX_USERS")
    private Integer maxUsers;

    @Column(name = "STORAGE_QUOTA_BYTES")
    private Long storageQuotaBytes;

    @Column(name = "REQUIRE_EMAIL_VERIFICATION")
    private Boolean requireEmailVerification;

    @Column(name = "PASSWORD_POLICY_JSON", columnDefinition = "TEXT")
    private String passwordPolicyJson;

    @Enumerated(EnumType.STRING)
    @Column(name = "SUBSCRIPTION_PLAN", length = 30)
    private SubscriptionPlan subscriptionPlan;

    @Column(name = "ONBOARDING_DATE")
    private LocalDate onboardingDate;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(name = "EXTRA_JSON", columnDefinition = "TEXT")
    private String extraJson;

    // ==========================================
    // 🧠 Lógica de Normalización (Elite Standard)
    // ==========================================

    @PrePersist
    @PreUpdate
    private void normalizeConfig() {
        // Normalización manejada por la clase base AuditableTenantEntity
    }
}
