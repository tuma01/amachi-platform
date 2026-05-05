package com.amachi.app.vitalia.medicalcatalog.consultation.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.vitalia.medicalcatalog.specialty.entity.MedicalSpecialty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Catálogo Global de Tipos de Consulta Médica (SaaS Elite Tier).
 * Define categorías estándar como Telemedicina, Urgencias, Control, etc.
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
    name = "CAT_MEDICAL_CONSULTATION_TYPE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_CONS_TYPE_CODE", columnNames = {"CODE"})
    }
)
public class MedicalConsultationType extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code;

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_SPECIALTY", foreignKey = @ForeignKey(name = "FK_MEDICAL_CONSULTATION_TYPE_SPECIALTY"))
    private MedicalSpecialty specialty;

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
