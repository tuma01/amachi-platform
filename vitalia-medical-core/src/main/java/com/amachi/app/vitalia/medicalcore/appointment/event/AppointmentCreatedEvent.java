package com.amachi.app.vitalia.medicalcore.appointment.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Domain event for when a new appointment is created (SaaS Elite Tier).
 */
@Getter
@RequiredArgsConstructor
public class AppointmentCreatedEvent extends DomainEvent {
    private final Long id;
    private final Long patientId;
    private final Long doctorId;
    private final OffsetDateTime startTime;
}
