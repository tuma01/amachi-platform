package com.amachi.app.core.domain.tenant.entity;

import com.amachi.app.core.common.enums.TenantType;
import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.theme.entity.Theme;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.SuperBuilder;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Table(name = "DMN_TENANT")
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GLOBAL")
public class Tenant extends Auditable<String> implements SoftDeletable, Model {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * SCHEMA COMPATIBILITY FIELDS
     * For Tenant, these are usually self-referential or null.
     */
    @Column(name = "TENANT_ID")
    @Builder.Default
    private Long tenantId = 0L;

    @Column(name = "TENANT_CODE", length = 50)
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

    // ID heredado de BaseEntity

    @NotBlank
    @Size(max = 100)
    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @NotBlank
    @Size(max = 100)
    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, length = 20, insertable = false, updatable = false)
    private TenantType type;// Ej: HOSPITAL, CLINIC, LABORATORY, PHARMACY, GLOBAL

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "DESCRIPTION")
    private String description;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "THEME_ID", nullable = true)
    private Theme theme;

    @Column(name = "ADDRESS_ID")
    private Long addressId;

    @Column(name = "LOGO_URL")
    private String logoUrl;

    @Column(name = "FAVICON_URL")
    private String faviconUrl;

    // ==========================================
    // 🧠 Lógica de Normalización (Elite Standard)
    // ==========================================

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) {
            this.code = this.code.trim().toUpperCase();
        }
        if (this.name != null) {
            this.name = this.name.trim();
        }
    }
}
