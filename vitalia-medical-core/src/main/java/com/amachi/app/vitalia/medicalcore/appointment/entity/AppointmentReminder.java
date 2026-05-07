package com.amachi.app.vitalia.medicalcore.appointment.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.ReminderStatus;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.ReminderChannel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Entity
@Table(name = "MED_APPOINTMENT_REMINDER", indexes = {
        @Index(name = "IDX_APP_REM_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_APP_REM_APPT",   columnList = "FK_ID_APPOINTMENT"),
        @Index(name = "IDX_APP_REM_STATUS",  columnList = "REMINDER_STATUS")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class AppointmentReminder extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_APPOINTMENT", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_REMINDER_APPOINTMENT"))
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(name = "CHANNEL", nullable = false, length = 30)
    private ReminderChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "REMINDER_STATUS", nullable = false, length = 50)
    private ReminderStatus status;

    @Column(name = "SCHEDULED_DATE", nullable = false)
    private LocalDateTime scheduledDate;

    @Column(name = "SENT_DATE")
    private LocalDateTime sentDate;

    @Column(name = "READ_DATE")
    private LocalDateTime readDate;

    @Column(name = "RETRY_COUNT")
    @Builder.Default
    private Integer retryCount = 0;

    @Column(name = "TARGET", length = 150)
    private String target;

    @Column(name = "EXTERNAL_MESSAGE_ID", length = 255)
    private String externalMessageId;

    @Column(name = "GATEWAY_RESPONSE", columnDefinition = "TEXT")
    private String gatewayResponse;
}
