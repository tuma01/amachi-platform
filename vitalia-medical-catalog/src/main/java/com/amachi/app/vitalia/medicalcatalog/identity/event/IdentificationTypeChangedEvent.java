package com.amachi.app.vitalia.medicalcatalog.identity.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Identification Type is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class IdentificationTypeChangedEvent extends DomainEvent {

    private final Long idTypeId;
    private final String code;
    private final String name;

}
