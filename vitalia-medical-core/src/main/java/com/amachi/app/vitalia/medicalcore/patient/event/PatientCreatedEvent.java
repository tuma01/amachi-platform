package com.amachi.app.vitalia.medicalcore.patient.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Published when a new Patient record is created in the Tenant (SaaS Elite Tier).
 * Listeners: Notification service, audit log, billing system.
 */
@Getter
@RequiredArgsConstructor
public class PatientCreatedEvent extends DomainEvent {
    private final Long id;
    private final String identificationNumber;
    private final Long personId;
}
