package com.amachi.app.core.common.event;

import com.amachi.app.core.common.context.TenantContext;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Clase base para todos los eventos de dominio de Amachi.
 * Incluye contexto cronológico y de multi-tenencia por diseño.
 */
@Getter
public abstract class DomainEvent {

    private final LocalDateTime occurredAt;
    private final Long tenantId;

    protected DomainEvent() {
        this.occurredAt = LocalDateTime.now();
        this.tenantId = resolveTenantId();
    }

    protected DomainEvent(LocalDateTime occurredAt, Long tenantId) {
        this.occurredAt = occurredAt != null ? occurredAt : LocalDateTime.now();
        this.tenantId = tenantId;
    }

    private Long resolveTenantId() {
        try {
            return TenantContext.getTenantId();
        } catch (IllegalStateException ex) {
            // ✔ GLOBAL context → no tenant
            return null;
        }
    }
}
