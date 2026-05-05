package com.amachi.app.vitalia.medicalcatalog.diagnosis.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidad Icd10 (Global Catalog).
 * Catálogo médico compartido para toda la plataforma.
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
    name = "CAT_ICD10",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_ICD10_CODE", columnNames = { "CODE" })
    },
    indexes = {
        @Index(name = "IDX_ICD10_CODE", columnList = "CODE")
    }
)
public class Icd10 extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Description {err.mandatory}")
    @Column(name = "DESCRIPTION", nullable = false, length = 500)
    private String description;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) this.code = this.code.trim().toUpperCase();
        if (this.description != null) this.description = this.description.trim();
    }
}
