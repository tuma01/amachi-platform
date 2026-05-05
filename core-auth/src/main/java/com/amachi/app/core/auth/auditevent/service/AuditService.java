package com.amachi.app.core.auth.auditevent.service;

import com.amachi.app.core.common.enums.AuditEventType;

public interface AuditService {

    void registerEvent(
            AuditEventType type,
            Long userId,
            Long tenantId,
            String tenantCode,
            String message
    );

    void registerEvent(
            AuditEventType type,
            Long userId,
            Long tenantId,
            String tenantCode,
            String message,
            String ip
    );
}
