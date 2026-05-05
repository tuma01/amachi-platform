package com.amachi.app.vitalia.medicalcatalog.demographic.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Civil Status is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class CivilStatusChangedEvent extends DomainEvent {

    private final Long civilStatusId;
    private final String code;
    private final String name;

}
