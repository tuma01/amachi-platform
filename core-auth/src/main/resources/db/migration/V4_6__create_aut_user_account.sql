-- ============================================================
-- Script: V4_6__create_aut_user_account.sql
-- Módulo: core-auth
-- Descripción: Tabla para cuentas de usuario vinculadas a un Tenant.
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS AUT_USER_ACCOUNT (

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
    FK_ID_TENANT        BIGINT NOT NULL,
    FK_ID_USER          BIGINT NOT NULL,
    FK_ID_PERSON        BIGINT NOT NULL,

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints & FK
    -- ==========================================
    -- FK_ID_TENANT: referencia de negocio al Tenant (BIGINT -> DMN_TENANT.ID BIGINT), compatible.
    CONSTRAINT FK_USER_ACCOUNT_USER   FOREIGN KEY (FK_ID_USER)       REFERENCES AUT_USER(ID),
    CONSTRAINT FK_USER_ACCOUNT_TENANT FOREIGN KEY (FK_ID_TENANT)     REFERENCES DMN_TENANT(ID),
    CONSTRAINT FK_USER_ACCOUNT_PERSON FOREIGN KEY (FK_ID_PERSON)     REFERENCES DMN_PERSON(ID),
    CONSTRAINT UK_USER_TENANT_ACCOUNT UNIQUE (FK_ID_USER, TENANT_ID),

    -- ==========================================
    -- INDEXES (SaaS Elite Tier - Atómico)
    -- ==========================================
    INDEX IDX_USER_ACCOUNT_TENANT (TENANT_ID),
    INDEX IDX_USER_ACCOUNT_PERSON (FK_ID_PERSON)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

