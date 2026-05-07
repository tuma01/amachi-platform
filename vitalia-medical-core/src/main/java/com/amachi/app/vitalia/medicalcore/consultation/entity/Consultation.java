package com.amachi.app.vitalia.medicalcore.consultation.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import com.amachi.app.vitalia.medicalcore.common.enums.ConsultationStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.OffsetDateTime;

@Entity
@Table(name = "MED_CONSULTATION", indexes = {
    @Index(name = "IDX_CONS_TENANT",  columnList = "TENANT_ID"),
    @Index(name = "IDX_CONS_PATIENT", columnList = "FK_ID_PATIENT"),
    @Index(name = "IDX_CONS_DATE",    columnList = "VISIT_DATETIME")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Consultation extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_CONS_PATIENT"))
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DOCTOR", foreignKey = @ForeignKey(name = "FK_MED_CONS_DOCTOR"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_CONS_HISTORY"))
    private MedicalHistory medicalHistory;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_CONSULTATION_TYPE", foreignKey = @ForeignKey(name = "FK_MED_CONS_TYPE"))
    private MedicalConsultationType type;

    @Column(name = "VISIT_DATETIME", nullable = false)
    private OffsetDateTime consultationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "VISIT_STATUS", nullable = false, length = 50)
    @Builder.Default
    private ConsultationStatus status = ConsultationStatus.SCHEDULED;

    @Column(name = "TRIAGE_LEVEL", length = 30)
    private String triageLevel;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.triageLevel != null) this.triageLevel = this.triageLevel.trim().toUpperCase();
        if (this.consultationDate == null) this.consultationDate = OffsetDateTime.now();
    }
}
