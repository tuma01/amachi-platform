package com.amachi.app.vitalia.medicalcore.patient.entity;

import com.amachi.app.vitalia.medicalcatalog.bloodtype.entity.BloodType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;

/**
 * Detalles biométricos y clínicos específicos del paciente (SaaS Elite Tier).
 */
@Embeddable
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PatientDetails {

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_BLOOD_TYPE", foreignKey = @ForeignKey(name = "FK_MED_PAT_BLOOD"))
    @Schema(description = "Grupo sanguíneo vinculado al catálogo")
    private BloodType bloodType;

    @Schema(description = "Peso en kilogramos", example = "75.50")
    @Column(name = "WEIGHT", precision = 5, scale = 2)
    private BigDecimal weight;

    @Schema(description = "Estatura en metros", example = "1.75")
    @Column(name = "HEIGHT", precision = 5, scale = 2)
    private BigDecimal height;

    @Schema(description = "Indicador de discapacidad", example = "false")
    @Column(name = "HAS_DISABILITY")
    private Boolean hasDisability;

    @Schema(description = "Detalles de la discapacidad", example = "Discapacidad motriz leve")
    @Column(name = "DISABILITY_DETAILS", length = 250)
    private String disabilityDetails;

    @Schema(description = "Estado de embarazo", example = "false")
    @Column(name = "IS_PREGNANT")
    private Boolean isPregnant;

    @Schema(description = "Semanas de embarazo", example = "12")
    @Column(name = "GESTATIONAL_WEEKS")
    private Integer gestationalWeeks;

    @Schema(description = "Número de hijos", example = "2")
    @Column(name = "CHILDREN_COUNT")
    private Integer childrenCount;

    @Schema(description = "Grupo étnico de auto-identificación", example = "Hispano")
    @Column(name = "ETHNIC_GROUP", length = 100)
    private String ethnicGroup;
}
