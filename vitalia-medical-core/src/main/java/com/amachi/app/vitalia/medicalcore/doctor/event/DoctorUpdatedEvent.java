package com.amachi.app.vitalia.medicalcore.doctor.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Evento de dominio: Médico actualizado (SaaS Elite Tier).
 */
@Getter
@RequiredArgsConstructor
public class DoctorUpdatedEvent extends DomainEvent {
    private final Long id;
    private final String licenseNumber;
    private final Boolean isActive;
}
