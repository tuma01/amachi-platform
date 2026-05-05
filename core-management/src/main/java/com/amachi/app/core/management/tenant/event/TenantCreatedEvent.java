package com.amachi.app.core.management.tenant.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;

/**
 * Event published when a new Tenant/Organization is created in the platform.
 */
@Getter
public class TenantCreatedEvent extends DomainEvent {

    private final Long createdTenantId;
    private final String code;
    private final String name;

    public TenantCreatedEvent(Long createdTenantId, String code, String name) {
        super();
        this.createdTenantId = createdTenantId;
        this.code = code;
        this.name = name;
    }
}
