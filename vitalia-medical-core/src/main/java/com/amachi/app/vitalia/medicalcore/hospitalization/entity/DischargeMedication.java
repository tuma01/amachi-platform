package com.amachi.app.vitalia.medicalcore.hospitalization.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "MED_DISCHARGE_MEDICATION", indexes = {
    @Index(name = "IDX_DIS_MED_TENANT", columnList = "TENANT_ID"),
    @Index(name = "IDX_DIS_MED_HOSP",   columnList = "FK_ID_HOSPITALIZATION")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class DischargeMedication extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_HOSPITALIZATION", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_DIS_MED_HOSP"))
    private Hospitalization hospitalization;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_MEDICATION", foreignKey = @ForeignKey(name = "FK_MED_DIS_MED_CAT"))
    private Medication medication;

    @Column(name = "MEDICATION_NAME_DISPLAY", length = 250)
    private String medicationName;

    @Column(name = "DOSAGE", length = 100)
    private String dosage;

    @Column(name = "FREQUENCY", length = 100)
    private String frequency;

    @Column(name = "DURATION", length = 100)
    private String duration;

    @Column(name = "INSTRUCTIONS", columnDefinition = "TEXT")
    private String instructions;
}
