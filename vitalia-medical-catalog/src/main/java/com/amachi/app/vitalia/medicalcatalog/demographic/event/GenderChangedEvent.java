package com.amachi.app.vitalia.medicalcatalog.demographic.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Gender is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class GenderChangedEvent extends DomainEvent {

    private final Long genderId;
    private final String code;
    private final String name;

}
