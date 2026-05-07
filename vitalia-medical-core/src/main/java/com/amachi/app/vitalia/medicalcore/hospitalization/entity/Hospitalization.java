package com.amachi.app.vitalia.medicalcore.hospitalization.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.AdmissionType;
import com.amachi.app.vitalia.medicalcore.common.enums.DischargeStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationPriority;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "MED_HOSPITALIZATION", indexes = {
    @Index(name = "IDX_HOSP_TENANT",  columnList = "TENANT_ID"),
    @Index(name = "IDX_HOSP_PATIENT", columnList = "FK_ID_PATIENT"),
    @Index(name = "IDX_HOSP_STATUS",  columnList = "HOSPITALIZATION_STATUS"),
    @Index(name = "IDX_HOSP_DATE",    columnList = "ADMISSION_DATE")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Hospitalization extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_HOSP_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER", foreignKey = @ForeignKey(name = "FK_MED_HOSP_ENCOUNTER"))
    private Encounter encounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DR_RESP", foreignKey = @ForeignKey(name = "FK_MED_HOSP_DR_RESP"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_NS_RESP", foreignKey = @ForeignKey(name = "FK_MED_HOSP_NS_RESP"))
    private Nurse nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_UNIT", foreignKey = @ForeignKey(name = "FK_MED_HOSP_UNIT"))
    private DepartmentUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ROOM", foreignKey = @ForeignKey(name = "FK_MED_HOSP_ROOM"))
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_BED", foreignKey = @ForeignKey(name = "FK_MED_HOSP_BED"))
    private Bed bed;

    @Column(name = "ADMISSION_DATE", nullable = false)
    private OffsetDateTime admissionDate;

    @Column(name = "DISCHARGE_DATE")
    private OffsetDateTime dischargeDate;

    @Column(name = "ESTIMATED_DISCHARGE_DATE")
    private OffsetDateTime estimatedDischargeDate;

    @Column(name = "FOLLOW_UP_DATE")
    private LocalDate followUpDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "HOSPITALIZATION_STATUS", nullable = false, length = 50)
    private HospitalizationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "HOSPITALIZATION_PRIORITY", length = 30)
    private HospitalizationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCHARGE_STATUS", length = 30)
    private DischargeStatus dischargeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "ADMISSION_TYPE", length = 50)
    private AdmissionType admissionType;

    @Column(name = "ADMISSION_REASON", length = 1000)
    private String admissionReason;

    @Column(name = "DISCHARGE_REASON", length = 1000)
    private String dischargeReason;

    @Column(name = "ADMISSION_DIAGNOSIS", length = 500)
    private String admissionDiagnosis;

    @Column(name = "FINAL_DIAGNOSIS", length = 500)
    private String finalDiagnosis;

    @Column(name = "TREATMENT_PLAN", columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(name = "DIAGNOSIS_NOTES", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(name = "DISCHARGE_INSTRUCTIONS", columnDefinition = "TEXT")
    private String dischargeInstructions;

    @Column(name = "OBSERVATIONS", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "INSURANCE_AUTH_NUMBER", length = 100)
    private String insuranceAuthorizationNumber;

    @Column(name = "TOTAL_COST", precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "CURRENCY", length = 10)
    @Builder.Default
    private String currency = "USD";

    @Transient
    public long getLengthOfStayInDays() {
        if (this.admissionDate == null) return 0;
        OffsetDateTime endDate = (this.dischargeDate != null) ? this.dischargeDate : OffsetDateTime.now();
        return Duration.between(this.admissionDate, endDate).toDays();
    }

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.admissionReason != null) this.admissionReason = this.admissionReason.trim();
        if (this.admissionDiagnosis != null) this.admissionDiagnosis = this.admissionDiagnosis.trim();
        if (this.admissionDate == null) this.admissionDate = OffsetDateTime.now();
    }
}
