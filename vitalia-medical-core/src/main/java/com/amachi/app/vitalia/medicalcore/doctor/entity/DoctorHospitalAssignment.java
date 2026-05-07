package com.amachi.app.vitalia.medicalcore.doctor.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.hospital.entity.Hospital;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Entity
@Table(
    name = "MED_DOCTOR_HOSPITAL_ASSIGN",
    indexes = {
        @Index(name = "IDX_DHA_TENANT",   columnList = "TENANT_ID"),
        @Index(name = "IDX_DHA_DOCTOR",   columnList = "FK_ID_DOCTOR"),
        @Index(name = "IDX_DHA_HOSPITAL", columnList = "FK_ID_HOSPITAL")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class DoctorHospitalAssignment extends AuditableTenantEntity implements Model {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DOCTOR", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_DHA_DOCTOR"))
    private Doctor doctor;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_HOSPITAL", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_DHA_HOSPITAL"))
    private Hospital hospital;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "IS_PRIMARY", nullable = false)
    @Builder.Default
    private Boolean isPrimary = false;

    @Column(name = "ROLE_IN_HOSPITAL", length = 100)
    private String roleInHospital;

    @PrePersist
    private void defaults() {
        if (this.startDate == null) this.startDate = LocalDate.now();
    }
}
