package com.amachi.app.vitalia.medicalcatalog.infrastructure.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a Medical Unit Type is changed in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class MedicalUnitTypeChangedEvent extends DomainEvent {

    private final Long unitTypeId;
    private final String code;
    private final String name;

}
