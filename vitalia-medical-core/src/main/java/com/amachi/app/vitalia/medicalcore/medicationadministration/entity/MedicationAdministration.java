package com.amachi.app.vitalia.medicalcore.medicationadministration.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.AdministrationStatus;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Registro de administración de medicamento (FHIR MedicationAdministration).
 * Representa el acto de administrar una dosis al paciente (enfermería/médico).
 */
@Entity
@Table(name = "MED_MEDICATION_ADMINISTRATION", indexes = {
    @Index(name = "IDX_MED_ADM_TENANT",  columnList = "TENANT_ID"),
    @Index(name = "IDX_MED_ADM_PATIENT", columnList = "FK_ID_PATIENT"),
    @Index(name = "IDX_MED_ADM_RX",      columnList = "FK_ID_PRESCRIPTION")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
@Schema(description = "Registro de administración de medicamento (FHIR MedicationAdministration)")
public class MedicationAdministration extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_ADM_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PRESCRIPTION", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_ADM_PRESCRIPTION"))
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DISPENSE",
                foreignKey = @ForeignKey(name = "FK_MED_ADM_DISPENSE"))
    private MedicationDispense dispense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER",
                foreignKey = @ForeignKey(name = "FK_MED_ADM_ENCOUNTER"))
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_NURSE",
                foreignKey = @ForeignKey(name = "FK_MED_ADM_NURSE"))
    private Nurse nurse;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private AdministrationStatus status;

    @Column(name = "ADMINISTERED_AT", nullable = false)
    private OffsetDateTime administeredAt;

    @Column(name = "DOSE_QUANTITY", precision = 10, scale = 2)
    private BigDecimal doseQuantity;

    @Column(name = "DOSE_UNIT", length = 30)
    private String doseUnit;

    @Column(name = "ROUTE", length = 50)
    private String route;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    private void onCreate() {
        if (this.administeredAt == null) this.administeredAt = OffsetDateTime.now();
        if (this.status == null) this.status = AdministrationStatus.COMPLETED;
    }
}
