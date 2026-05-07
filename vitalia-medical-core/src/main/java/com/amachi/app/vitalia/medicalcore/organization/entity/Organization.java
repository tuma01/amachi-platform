package com.amachi.app.vitalia.medicalcore.organization.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "MED_ORGANIZATION",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_ORG_LEGAL_TENANT", columnNames = {"TENANT_ID", "LEGAL_IDENTIFIER"})
    },
    indexes = {
        @Index(name = "IDX_MED_ORG_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_MED_ORG_TYPE",   columnList = "TENANT_ID, TYPE")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Organization extends AuditableTenantEntity implements Model {

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "LEGAL_IDENTIFIER", length = 50)
    private String legalIdentifier;

    @Column(name = "TYPE", length = 50)
    private String type;

    @Column(name = "ADDRESS", length = 250)
    private String address;

    @Column(name = "CONTACT_PHONE", length = 50)
    private String contactPhone;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "WEBSITE", length = 200)
    private String website;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim().toUpperCase();
        if (legalIdentifier != null) legalIdentifier = legalIdentifier.trim().toUpperCase();
        if (type != null) type = type.trim().toUpperCase();
    }
}
