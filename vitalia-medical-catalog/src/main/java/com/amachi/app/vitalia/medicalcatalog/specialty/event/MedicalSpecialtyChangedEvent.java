package com.amachi.app.vitalia.medicalcatalog.specialty.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Medical Specialty is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class MedicalSpecialtyChangedEvent extends DomainEvent {

    private final Long specialtyId;
    private final String code;
    private final String name;

}
