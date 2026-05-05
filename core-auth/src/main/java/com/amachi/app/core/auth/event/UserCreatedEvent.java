package com.amachi.app.core.auth.event;

import com.amachi.app.core.common.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Event published when a new Global User is created.
 * tenantId is set to "SYSTEM" because user identity is a platform-level concept.
 */
@Getter
public class UserCreatedEvent extends DomainEvent {

    private final Long userId;
    private final String email;

    public UserCreatedEvent(Long userId, String email) {
        // Global Identity context: users are platform-level, not tenant-scoped
        super(LocalDateTime.now(), 0L);
        this.userId = userId;
        this.email = email;
    }
}
