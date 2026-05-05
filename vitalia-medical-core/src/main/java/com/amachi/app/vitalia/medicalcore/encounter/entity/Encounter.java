package com.amachi.app.vitalia.medicalcore.encounter.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterType;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "MED_ENCOUNTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Audited
public class Encounter extends AuditableTenantEntity implements Model {

    @Column(name = "ENCOUNTER_DATE")
    private OffsetDateTime encounterDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_ENCOUNTER_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DOCTOR", foreignKey = @ForeignKey(name = "FK_ENCOUNTER_DOCTOR"))
    private Doctor doctor;

//    ----------------------------------------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_APPOINTMENT", foreignKey = @ForeignKey(name = "FK_ENCOUNTER_APPOINTMENT"))
    private Appointment appointment;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_CONSULTATION_TYPE", foreignKey = @ForeignKey(name = "FK_ENC_CONS_TYPE"))
//    private MedicalConsultationType consultationType;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_HISTORY", foreignKey = @ForeignKey(name = "FK_ENC_HISTORY"))
//    private MedicalHistory medicalHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_EPISODEOFCARE", foreignKey = @ForeignKey(name = "FK_ENCOUNTER_EPISODE_OF_CARE"))
    private EpisodeOfCare episodeOfCare;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    @Builder.Default
    private EncounterStatus status = EncounterStatus.PLANNED;

    @Enumerated(EnumType.STRING)
    @Column(name = "ENCOUNTER_TYPE", nullable = false, length = 30)
    private EncounterType encounterType;

//    @Column(name = "TRIAGE_LEVEL", length = 50)
//    private String triageLevel;
//
//
//
//    @Column(name = "END_TIME")
//    private OffsetDateTime endTime;

    @Column(name = "CHIEF_COMPLAINT", columnDefinition = "TEXT")
    private String chiefComplaint;

//    @Column(name = "SYMPTOMS", columnDefinition = "TEXT")
//    private String symptoms;
//
//    @Column(name = "DIAGNOSIS_NOTES", columnDefinition = "TEXT")
//    private String diagnosisNotes;
//
//    @Column(name = "TREATMENT_PLAN", columnDefinition = "TEXT")
//    private String treatmentPlan;
//
//    @Column(name = "RECOMMENDATIONS", columnDefinition = "TEXT")
//    private String recommendations;

    @Column(name = "CLINICAL_NOTES", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "DURATION_MINUTES")
    private Integer durationMinutes;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Condition> conditions = new ArrayList<>();

//    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
//    @Builder.Default
//    private List<Observation> observations = new ArrayList<>();
//
    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Prescription> prescriptions = new ArrayList<>();

    public boolean hasDiagnosis() {
        return conditions != null &&
                conditions.stream()
                        .anyMatch(c -> !Boolean.TRUE.equals(c.getDeleted()));
    }

    public OffsetDateTime getEncounterDateTime() {
        return encounterDate;
    }

    @PrePersist
    public void onPrePersist() {
        super.onPrePersist(); // 🔥 obligatorio

        if (this.encounterDate == null) {
            this.encounterDate = OffsetDateTime.now();
        }
    }

    public void complete() {
        if (this.status != EncounterStatus.IN_PROGRESS) {
            throw new BusinessException("encounter.error.must.be.in.progress");
        }
        if (!hasDiagnosis()) {
            throw new BusinessException("encounter.error.diagnosis.required");
        }
        this.status = EncounterStatus.COMPLETED;
        this.durationMinutes = (int) Duration
                .between(this.encounterDate, OffsetDateTime.now())
                .toMinutes();
    }

    public void cancel(String reason) {
        if (this.status == EncounterStatus.COMPLETED) {
            throw new BusinessException("encounter.error.cannot.cancel.completed");
        }
        this.status = EncounterStatus.CANCELLED;

        String current = this.notes != null ? this.notes : "";
        this.notes = current + " | CANCEL: " + reason;
    }
}
