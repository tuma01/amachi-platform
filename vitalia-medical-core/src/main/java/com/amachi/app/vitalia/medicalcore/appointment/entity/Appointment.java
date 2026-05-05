package com.amachi.app.vitalia.medicalcore.appointment.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;

import com.amachi.app.vitalia.medicalcore.common.enums.AppointmentSource;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

import org.hibernate.envers.Audited;

@SuperBuilder
@Entity
@Table(name = "MED_APPOINTMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
public class Appointment extends AuditableTenantEntity implements Model {

    @Column(name = "START_TIME", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private OffsetDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "SOURCE", length = 30)
    @Builder.Default
    private AppointmentSource source = AppointmentSource.WALK_IN;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PATIENT", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_APPOINTMENT_PATIENT"))
    private Patient patient;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DOCTOR", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_APPOINTMENT_DOCTOR"))
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ENCOUNTER", foreignKey = @ForeignKey(name = "FK_MED_APPOINTMENT_ENCOUNTER"))
    private Encounter encounter;

// Juan: simple implementacion
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_UNIT", foreignKey = @ForeignKey(name = "FK_MED_APP_UNIT"))
//    private DepartmentUnit unit;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_ROOM", foreignKey = @ForeignKey(name = "FK_MED_APP_ROOM"))
//    private Room room;

    @Column(name = "APPOINTMENT_TYPE", length = 50)
    private String type;

    @Column(name = "PRIORITY", length = 20)
    @Builder.Default
    private String priority = "NORMAL";

    @Column(name = "REASON", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "NO_SHOW")
    @Builder.Default
    private Boolean noShow = false;

    // --- Timestamps de Proceso ---
    @Column(name = "CHECKED_IN_AT")
    private OffsetDateTime checkedInAt;

    @Column(name = "COMPLETED_AT")
    private OffsetDateTime completedAt;

    @Column(name = "CANCELLED_AT")
    private OffsetDateTime cancelledAt;

    @Column(name = "CANCEL_REASON", columnDefinition = "TEXT")
    private String cancelReason;

    // --- Control de Concurrencia (SaaS Elite) ---
    @Column(name = "LOCKED_UNTIL")
    private OffsetDateTime lockedUntil;

    @Column(name = "LOCKED_BY", length = 100)
    private String lockedBy;
}
