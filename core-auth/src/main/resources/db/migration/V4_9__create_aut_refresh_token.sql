-- ============================================================
-- Script: V4_9__create_aut_refresh_token.sql
-- Módulo: core-auth
-- Descripción: Tabla para tokens de refresco de sesión.
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS AUT_REFRESH_TOKEN (

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
    -- Relaciones
    -- ==========================================
    FK_ID_USER          BIGINT NOT NULL,

    -- ==========================================
    -- Datos de Token
    -- ==========================================
    TOKEN               VARCHAR(255) NOT NULL,
    EXPIRY_DATE         DATETIME(6) NOT NULL,
    CREATED_AT         DATETIME(6) NOT NULL,
    REVOKED             BOOLEAN DEFAULT FALSE NOT NULL,

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
    CONSTRAINT UK_REFRESH_TOKEN_TOKEN UNIQUE (TOKEN),
    
    CONSTRAINT FK_REFRESH_TOKEN_USER
        FOREIGN KEY (FK_ID_USER) REFERENCES AUT_USER (ID)
        ON DELETE CASCADE,

    CONSTRAINT FK_REFRESH_TOKEN_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT (ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- ==========================================
    -- INDEXES (SaaS Elite Tier - Atómico)
    -- ==========================================
    INDEX IDX_REFRESH_TOKEN_USER    (FK_ID_USER),
    INDEX IDX_REFRESH_TOKEN_TENANT  (TENANT_ID),
    INDEX IDX_REFRESH_TOKEN_TOKEN   (TOKEN)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

