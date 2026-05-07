package com.amachi.app.vitalia.medicalcore.medicalhistory.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicalHistoryStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MED_MEDICAL_HISTORY", indexes = {
        @Index(name = "IDX_HIST_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_HIST_PATIENT", columnList = "FK_ID_PATIENT"),
        @Index(name = "IDX_HIST_NUMBER",  columnList = "HISTORY_NUMBER")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class MedicalHistory extends AuditableTenantEntity implements Model {

    @Column(name = "HISTORY_NUMBER", nullable = false, length = 50)
    private String historyNumber;

    @Column(name = "DOCUMENT_UUID", length = 36)
    private String documentUuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_HIST_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PERSON",
                foreignKey = @ForeignKey(name = "FK_MED_HIST_PERSON"))
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DR_RESP",
                foreignKey = @ForeignKey(name = "FK_MED_HIST_DR_RESP"))
    private Doctor responsibleDoctor;

    @Column(name = "RECORD_DATE", nullable = false)
    private LocalDate recordDate;

    @Column(name = "VALID_UNTIL")
    private LocalDate validUntil;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    @Builder.Default
    private MedicalHistoryStatus status = MedicalHistoryStatus.ACTIVE;

    @Column(name = "CONFIDENTIALITY_LEVEL", length = 30)
    @Builder.Default
    private String confidentialityLevel = "NORMAL";

    @Column(name = "IS_ORGAN_DONOR", nullable = false)
    @Builder.Default
    private Boolean isOrganDonor = false;

    @Column(name = "IS_LOCKED", nullable = false)
    @Builder.Default
    private Boolean isLocked = false;

    @Column(name = "OBSERVATIONS", columnDefinition = "TEXT")
    private String observations;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "medicalHistory", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Encounter> encounters = new ArrayList<>();

    @PrePersist @PreUpdate
    private void normalize() {
        if (historyNumber != null) historyNumber = historyNumber.trim().toUpperCase();
        if (observations != null) observations = observations.trim();
        if (notes != null) notes = notes.trim();
        if (recordDate == null) recordDate = LocalDate.now();
    }
}
