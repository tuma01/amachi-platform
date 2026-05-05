package com.amachi.app.vitalia.medicalcore.appointment.event;

import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Domain event published when an appointment is updated (SaaS Elite Tier).
 * Enables downstream handlers (notifications, analytics, audit trail) to react
 * without coupling them to the core scheduling logic.
 */
@Getter
@RequiredArgsConstructor
public class AppointmentUpdatedEvent extends DomainEvent {
    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final OffsetDateTime startTime;
    private final AppointmentStatus status;
}
