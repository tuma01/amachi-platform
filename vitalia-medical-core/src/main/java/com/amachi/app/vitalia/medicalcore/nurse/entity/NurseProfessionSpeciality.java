package com.amachi.app.vitalia.medicalcore.nurse.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "MED_NURSE_PROF_SPECIALITY",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_NPS_TENANT_NAME",
            columnNames = {"TENANT_ID", "NAME"})
    },
    indexes = {
        @Index(name = "IDX_NPS_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_NPS_NAME",   columnList = "NAME")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class NurseProfessionSpeciality extends AuditableTenantEntity implements Model {

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @PrePersist @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim();
    }
}
