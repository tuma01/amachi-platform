package com.amachi.app.vitalia.medicalcatalog.procedure.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;


/**
 * Medical Procedure Entity (Global Catalog).
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
    name = "CAT_MEDICAL_PROCEDURE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_PROCEDURE_CODE", columnNames = {"CODE"})
    },
    indexes = {
        @Index(name = "IDX_PROC_CODE", columnList = "CODE")
    }
)

public class MedicalProcedure extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 500)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 50)
    private String type; // e.g., LABORATORY, SURGERY, IMAGING, CONSULTATION

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
