package com.amachi.app.vitalia.medicalcore.doctor.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Evento de dominio: Médico creado (SaaS Elite Tier).
 */
@Getter
@RequiredArgsConstructor
public class DoctorCreatedEvent extends DomainEvent {
    private final Long id;
    private final String externalId;
    private final String licenseNumber;
    private final Long personId;
}
