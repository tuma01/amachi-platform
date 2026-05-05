package com.amachi.app.vitalia.medicalcore.prescription.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicationRequestStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Orden de medicación o receta electrónica alineada al estándar FHIR Prescription (SaaS Elite Tier).
 * Representa la prescripción digital emitida por un facultativo.
 */
@Entity
@Table(name = "MED_PRESCRIPTION", indexes = {
    @Index(name = "IDX_PRESCRIPTION_TENANT", columnList = "TENANT_ID"),
    @Index(name = "IDX_PRESCRIPTION_PATIENT", columnList = "FK_ID_PATIENT"),
    @Index(name = "IDX_PRESCRIPTION_DOC", columnList = "FK_ID_DOCTOR")
})
@Getter
@SuperBuilder @Setter
@NoArgsConstructor @AllArgsConstructor

@EqualsAndHashCode(callSuper = true)
@Audited
@Schema(description = "Registro de orden o receta médica (FHIR Prescription)")
public class Prescription extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_PRESCRIPTION_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER", foreignKey = @ForeignKey(name = "FK_PRESCRIPTION_ENCOUNTER"))
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_DOCTOR", nullable = false, foreignKey = @ForeignKey(name = "FK_PRESCRIPTION_DOCTOR"))
    private Doctor doctor;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_MEDICATION", foreignKey = @ForeignKey(name = "FK_PRESCRIPTION_MEDICATION"))
    private Medication medication;

    @Column(name = "MEDICATION_DISPLAY_NAME", length = 250)
    private String medicationDisplayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private MedicationRequestStatus status;

    @Column(name = "AUTHORED_ON", nullable = false)
    private OffsetDateTime authoredOn;

    @Column(name = "DOSAGE_INSTRUCTION", columnDefinition = "TEXT")
    private String dosageInstruction;

    @Column(name = "ROUTE", length = 50)
    private String route;

    @Column(name = "FREQUENCY", length = 50)
    private String frequency;

    @Column(name = "QUANTITY", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "PRIORITY", length = 20)
    private String priority;

    @Column(name = "REASON_CODE", length = 100)
    private String reasonCode;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    private void onCreate() {
        if (this.authoredOn == null) {
            this.authoredOn = OffsetDateTime.now();
        }
        normalize();
    }

    @PreUpdate
    private void onUpdate() {
        normalize();
    }

    private void normalize() {
        if (this.medicationDisplayName != null)
            this.medicationDisplayName = this.medicationDisplayName.trim();
        if (this.dosageInstruction != null)
            this.dosageInstruction = this.dosageInstruction.trim();
        if (this.route != null)
            this.route = this.route.trim().toUpperCase();
        if (this.frequency != null)
            this.frequency = this.frequency.trim().toUpperCase();
    }
}
