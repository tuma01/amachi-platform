package com.amachi.app.vitalia.medicalcore.patient.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Published when a Patient's status or clinical data is updated (SaaS Elite Tier).
 * Listeners: Notification service, clinical audit log.
 */
@Getter
@RequiredArgsConstructor
public class PatientUpdatedEvent extends DomainEvent {
    private final Long id;
    private final String identificationNumber;
}
