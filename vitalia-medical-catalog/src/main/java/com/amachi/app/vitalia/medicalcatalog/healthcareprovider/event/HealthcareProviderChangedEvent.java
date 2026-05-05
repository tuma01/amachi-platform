package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event published when a new Healthcare Provider is created in the catalog.
 */
@Getter
@RequiredArgsConstructor
public class HealthcareProviderChangedEvent extends DomainEvent {

    private final Long providerId;
    private final String code;
    private final String name;
    private final String taxId;

}
