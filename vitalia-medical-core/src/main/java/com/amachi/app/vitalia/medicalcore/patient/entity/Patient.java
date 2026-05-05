package com.amachi.app.vitalia.medicalcore.patient.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.PatientStatus;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "MED_PATIENT",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_PAT_IDENTITY_TENANT", columnNames = {"FK_ID_PERSON", "TENANT_ID", "IS_DELETED"}),
        @UniqueConstraint(name = "UK_MED_PAT_TENANT_NHC", columnNames = {"TENANT_ID", "NHC", "IS_DELETED"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Audited
public class Patient extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false, foreignKey = @ForeignKey(name = "FK_PATIENT_PERSON"))
    private Person person;

    @Column(name = "NHC", unique = true, nullable = false, length = 50)
    private String nhc;

    @Column(name = "IDENTIFICATION_NUMBER", length = 50)
    private String identificationNumber;

    @Column(name = "NATIONALITY", length = 100)
    private String nationality;

    @Column(name = "EDUCATION_LEVEL", length = 100)
    private String educationLevel;

    @Column(name = "OCCUPATION", length = 200)
    private String occupation;

    @Column(name = "PREFERRED_LANGUAGE", length = 10)
    private String preferredLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "PATIENT_STATUS", nullable = false, length = 30)
    @Builder.Default
    private PatientStatus patientStatus = PatientStatus.ACTIVE;

    @Column(name = "ALLERGY_SUMMARY", columnDefinition = "TEXT")
    private String allergySummary;

    @Column(name = "ADDITIONAL_REMARKS", columnDefinition = "TEXT")
    private String additionalRemarks;

    @Lob
    @Column(name = "PHOTO", columnDefinition = "LONGBLOB")
    private byte[] photo;

    // --- Emergency Contact ---
    @Column(name = "EMERGENCY_CONTACT_NAME", length = 200)
    private String emergencyContactName;

    @Column(name = "EMERGENCY_CONTACT_RELATION", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "EMERGENCY_CONTACT_PHONE", length = 50)
    private String emergencyContactPhone;

    @Column(name = "EMERGENCY_CONTACT_EMAIL", length = 100)
    private String emergencyContactEmail;

    @Column(name = "EMERGENCY_CONTACT_ADDRESS", columnDefinition = "TEXT")
    private String emergencyContactAddress;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Embedded
    private PatientDetails details;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", foreignKey = @ForeignKey(name = "FK_MED_PAT_HIST"))
//    private MedicalHistory medicalHistory;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.nhc != null)
            this.nhc = this.nhc.trim().toUpperCase();
    }
}
