package com.amachi.app.vitalia.medicalcore.medicationdispense.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcore.common.enums.DispenseStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Registro de dispensación de medicamento (FHIR MedicationDispense).
 * Representa la entrega física del medicamento prescrito al paciente.
 */
@Entity
@Table(name = "MED_MEDICATION_DISPENSE", indexes = {
    @Index(name = "IDX_MED_DISP_TENANT",  columnList = "TENANT_ID"),
    @Index(name = "IDX_MED_DISP_PATIENT", columnList = "FK_ID_PATIENT"),
    @Index(name = "IDX_MED_DISP_RX",      columnList = "FK_ID_PRESCRIPTION")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
@Schema(description = "Registro de dispensación de medicamento (FHIR MedicationDispense)")
public class MedicationDispense extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_DISP_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PRESCRIPTION", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_DISP_PRESCRIPTION"))
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER",
                foreignKey = @ForeignKey(name = "FK_MED_DISP_ENCOUNTER"))
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DISPENSER",
                foreignKey = @ForeignKey(name = "FK_MED_DISP_DISPENSER"))
    private Doctor dispenser;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_MEDICATION",
                foreignKey = @ForeignKey(name = "FK_MED_DISP_MEDICATION"))
    private Medication medication;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private DispenseStatus status;

    @Column(name = "QUANTITY", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "DAYS_SUPPLY")
    private Integer daysSupply;

    @Column(name = "LOT_NUMBER", length = 50)
    private String lotNumber;

    @Column(name = "DISPENSED_AT", nullable = false)
    private OffsetDateTime dispensedAt;

    @Column(name = "HANDED_OVER_AT")
    private OffsetDateTime handedOverAt;

    @Column(name = "DISPENSER_NOTE", columnDefinition = "TEXT")
    private String dispenserNote;

    @PrePersist
    private void onCreate() {
        if (this.dispensedAt == null) this.dispensedAt = OffsetDateTime.now();
        if (this.status == null) this.status = DispenseStatus.PREPARATION;
    }
}
