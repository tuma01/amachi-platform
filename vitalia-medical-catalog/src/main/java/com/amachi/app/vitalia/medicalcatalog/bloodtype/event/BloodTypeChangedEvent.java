package com.amachi.app.vitalia.medicalcatalog.bloodtype.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Blood Type is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class BloodTypeChangedEvent extends DomainEvent {

    private final Long bloodTypeId;
    private final String code;
    private final String name;

}
