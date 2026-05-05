-- ============================================================
-- Script: V4_11__create_aut_blacklisted_token.sql
-- Módulo: core-auth
-- Descripción: Tabla para tokens invalidados (logout/blacklist).
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS AUT_BLACKLISTED_TOKEN (

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
    -- Datos de Token
    -- ==========================================
    TOKEN               VARCHAR(500) NOT NULL,
    BLACKLISTED_AT      DATETIME(6) NOT NULL,
    EXPIRES_AT          DATETIME(6) NOT NULL,

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints & FK
    -- ==========================================
    CONSTRAINT UK_BLACKLISTED_TOKEN_TOKEN UNIQUE (TOKEN),

    CONSTRAINT FK_BLACKLISTED_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT (ID)
        ON UPDATE CASCADE,

    -- ==========================================
    -- INDEXES (SaaS Elite Tier - Atómico)
    -- ==========================================
    INDEX IDX_BLACKLISTED_TOKEN_TOKEN   (TOKEN),
    INDEX IDX_BLACKLISTED_TOKEN_TENANT  (TENANT_ID),
    INDEX IDX_BLACKLISTED_TOKEN_EXPIRY  (EXPIRES_AT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

