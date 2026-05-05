package com.amachi.app.vitalia.medicalcatalog.medication.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_MEDICATION",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MEDICATION_CODE", columnNames = { "CODE" })
    }
)
public class Medication extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 50)
    private String code;

    @NotBlank(message = "Generic Name {err.mandatory}")
    @Column(name = "GENERIC_NAME", nullable = false, length = 250)
    private String genericName;

    @Column(name = "COMMERCIAL_NAME", length = 250)
    private String commercialName;

    @Column(name = "CONCENTRATION", length = 100)
    private String concentration;

    @Column(name = "PHARMACEUTICAL_FORM", length = 100)
    private String pharmaceuticalForm;

    @Column(name = "PRESENTATION", length = 250)
    private String presentation;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) this.code = this.code.trim().toUpperCase();
        if (this.genericName != null) this.genericName = this.genericName.trim();
        if (this.commercialName != null) this.commercialName = this.commercialName.trim();
    }
}
