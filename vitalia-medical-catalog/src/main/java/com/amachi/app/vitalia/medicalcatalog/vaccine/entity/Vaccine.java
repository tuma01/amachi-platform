package com.amachi.app.vitalia.medicalcatalog.vaccine.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a Vaccine catalog entry (Global Catalog).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_VACCINE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_VACCINE_CODE", columnNames = {"CODE"})
    },
    indexes = {
        @Index(name = "IDX_VACCINE_CODE", columnList = "CODE")
    }
)
public class Vaccine extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code; // e.g., VAC-001

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 150)
    private String name; // e.g., Hepatitis B, Triple Viral

    @Column(name = "DESCRIPTION", length = 250)
    private String description;

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

