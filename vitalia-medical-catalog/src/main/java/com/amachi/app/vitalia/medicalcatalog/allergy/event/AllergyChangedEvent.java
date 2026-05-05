package com.amachi.app.vitalia.medicalcatalog.allergy.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Allergy is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class AllergyChangedEvent extends DomainEvent {

    private final Long allergyId;
    private final String code;
    private final String name;

}
