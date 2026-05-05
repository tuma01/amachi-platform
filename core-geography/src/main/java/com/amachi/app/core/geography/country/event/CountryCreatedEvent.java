package com.amachi.app.core.geography.country.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Event fired when a new country is created.
 */
@Getter
@RequiredArgsConstructor
public class CountryCreatedEvent extends DomainEvent {

    private final Long countryId;
    private final String name;
}
