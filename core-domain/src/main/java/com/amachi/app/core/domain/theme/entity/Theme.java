package com.amachi.app.core.domain.theme.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.common.enums.ThemeMode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.amachi.app.core.common.context.TenantContext.getTenantCode;
import static com.amachi.app.core.common.context.TenantContext.setTenantCode;

/**
 * Theme Entity (Tenant-specific UI Configuration).
 * SaaS Elite Tier Standard.
 */
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "DMN_THEME")
public class Theme extends Auditable<String> implements SoftDeletable, Model {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    /**
     * SCHEMA COMPATIBILITY FIELDS
     * Logically global, physically scoped in legacy DB.
     */
    @Column(name = "TENANT_ID", nullable = false)
    @Builder.Default
    private Long tenantId = 0L;

    @Column(name = "TENANT_CODE", nullable = false, length = 50)
    @Builder.Default
    private String tenantCode = "GLOBAL";

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

    @Column(name = "CODE", length = 50, nullable = false)
    private String code;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "LOGO_URL", length = 1000)
    private String logoUrl;

    @Column(name = "FAVICON_URL", length = 1000)
    private String faviconUrl;

    @Column(name = "PRIMARY_COLOR", length = 20)
    private String primaryColor;

    @Column(name = "SECONDARY_COLOR", length = 20)
    private String secondaryColor;

    @Column(name = "BACKGROUND_COLOR", length = 20)
    private String backgroundColor;

    @Column(name = "TEXT_COLOR", length = 20)
    private String textColor;

    @Column(name = "ACCENT_COLOR", length = 20)
    private String accentColor;

    @Column(name = "WARN_COLOR", length = 20)
    private String warnColor;

    @Column(name = "LINK_COLOR", length = 20)
    private String linkColor;

    @Column(name = "BUTTON_TEXT_COLOR", length = 20)
    private String buttonTextColor;

    @Column(name = "FONT_FAMILY", length = 100)
    private String fontFamily;

    @Enumerated(EnumType.STRING)
    @Column(name = "THEME_MODE", length = 10)
    private ThemeMode themeMode;

    @Column(name = "PROPERTIES_JSON", columnDefinition = "TEXT")
    private String propertiesJson;

    @Column(name = "CUSTOM_CSS", columnDefinition = "TEXT")
    private String customCss;

    @Column(name = "ALLOW_CUSTOM_CSS", nullable = false)
    @Builder.Default
    private boolean allowCustomCss = false;

    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "IS_TEMPLATE", nullable = false)
    @Builder.Default
    private boolean template = false;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) {
            this.code = this.code.trim().toUpperCase();
        }
        if (this.name != null) {
            this.name = this.name.trim();
        }
        if (getTenantCode() == null) {
            setTenantCode("SYSTEM");
        }
    }
}
