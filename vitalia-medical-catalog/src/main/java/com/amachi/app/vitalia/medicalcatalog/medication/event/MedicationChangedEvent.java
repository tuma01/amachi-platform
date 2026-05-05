package com.amachi.app.vitalia.medicalcatalog.medication.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Medication is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class MedicationChangedEvent extends DomainEvent {

    private final Long medicationId;
    private final String code;
    private final String genericName;

}
