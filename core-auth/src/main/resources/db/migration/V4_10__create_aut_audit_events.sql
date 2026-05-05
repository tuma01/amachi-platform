-- ============================================================
-- Script: V4_10__create_aut_audit_events.sql
-- Módulo: core-auth
-- Descripción: Tabla para eventos de auditoría de seguridad.
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS AUT_AUDIT_EVENTS (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50) NOT NULL,
    TENANT_ID           BIGINT NOT NULL,
    EXTERNAL_ID         VARCHAR(36) NOT NULL UNIQUE,
    VERSION             BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED          BOOLEAN DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Datos de Evento
    -- ==========================================
    EVENT_TYPE          VARCHAR(100) NOT NULL,
    FK_ID_USER          BIGINT NOT NULL,
    MESSAGE             VARCHAR(500) NOT NULL,
    TIMESTAMP           DATETIME(6) NOT NULL,
    IP_ADDRESS          VARCHAR(255) NULL,

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_AUDIT_EVENTS_USER (FK_ID_USER),
    INDEX IDX_AUDIT_EVENTS_TENANT (TENANT_ID),
    INDEX IDX_AUDIT_EVENTS_TYPE (EVENT_TYPE),
    INDEX IDX_AUDIT_EVENTS_TIMESTAMP (TIMESTAMP)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
