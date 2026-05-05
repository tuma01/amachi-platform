package com.amachi.app.core.auth.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Event published when a new Global Role is created.
 * tenantId is set to "SYSTEM" because roles are global cross-tenant entities.
 */
@Getter
public class RoleCreatedEvent extends DomainEvent {

    private final Long roleId;
    private final String name;

    public RoleCreatedEvent(Long roleId, String name) {
        // Global static context: roles are platform-level, not tenant-scoped
        super(LocalDateTime.now(), 0L);
        this.roleId = roleId;
        this.name = name;
    }
}
