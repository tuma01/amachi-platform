package com.amachi.app.vitalia.medicalcatalog.kinship.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Kinship is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class KinshipChangedEvent extends DomainEvent {

    private final Long kinshipId;
    private final String code;
    private final String name;

}
