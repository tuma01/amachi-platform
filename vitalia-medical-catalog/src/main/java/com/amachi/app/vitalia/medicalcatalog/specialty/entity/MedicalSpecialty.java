package com.amachi.app.vitalia.medicalcatalog.specialty.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Catálogo de Especialidades Médicas (Global Pool).
 * SaaS Elite Tier.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_MEDICAL_SPECIALTY",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_SPECIALTY_CODE", columnNames = {"CODE"})
    }
)
public class MedicalSpecialty extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "TARGET_PROFESSION", length = 20)
    @Builder.Default
    private String targetProfession = "BOTH"; // DOCTOR, NURSE, BOTH

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) this.code = this.code.trim().toUpperCase();
        if (this.name != null) this.name = this.name.trim();
    }
}
