package com.amachi.app.core.auth.auditevent.service.impl;

import com.amachi.app.core.auth.auditevent.entity.AuditEvent;
import com.amachi.app.core.auth.auditevent.repository.AuditEventRepository;
import com.amachi.app.core.auth.auditevent.service.AuditService;
import com.amachi.app.core.common.enums.AuditEventType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditEventRepository auditEventRepository;

    @Override
    public void registerEvent(AuditEventType type,
                              Long userId,
                              Long tenantId,
                              String tenantCode,
                              String message) {
        registerEvent(type, userId, tenantId, tenantCode, message, null);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void registerEvent(AuditEventType type,
                              Long userId,
                              Long tenantId,
                              String tenantCode,
                              String message,
                              String ipAddress) {

        try {
            AuditEvent event = AuditEvent.builder()
                    .eventType(type)
                    .userId(userId)
                    .tenantId(tenantId)
                    .tenantCode(tenantCode)
                    .message(message)
                    .ipAddress(ipAddress)
                    .build();

            auditEventRepository.saveAndFlush(event);

        } catch (Exception ex) {
            // No romper el flujo de negocio jamás por auditoría
            log.error("❌ [CRITICAL] Error persistiendo auditoría (Fail-Safe activado): {}", ex.getMessage());
        }
    }
}
