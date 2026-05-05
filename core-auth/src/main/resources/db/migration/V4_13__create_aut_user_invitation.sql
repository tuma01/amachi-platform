-- ============================================================
-- Script: V4_13__create_aut_user_invitation.sql
-- Módulo: core-auth
-- Descripción: Tabla de invitaciones de usuarios enviadas por TenantAdmin.
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
--
-- Ciclo de vida del STATUS:
--   PENDING   → Invitación enviada, esperando que el usuario complete el registro.
--   ACCEPTED  → El usuario completó el formulario de onboarding y fue activado.
--   EXPIRED   → El TTL del token expiró antes de que el usuario aceptara.
--   CANCELLED → El Admin revocó la invitación manualmente.
-- ============================================================

CREATE TABLE IF NOT EXISTS AUT_USER_INVITATION (

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
    FK_ID_USER          BIGINT NULL,
    FK_ID_ROLE          BIGINT NOT NULL,
    ROLE_CONTEXT        VARCHAR(30) NOT NULL,

    -- ==========================================
    -- Datos de Invitación
    -- ==========================================
    NATIONAL_ID         VARCHAR(30),
    EMAIL               VARCHAR(120) NOT NULL,
    TOKEN               VARCHAR(255) NOT NULL,
    EXPIRES_AT          DATETIME(6) NOT NULL,
    STATUS              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    ACCEPTED_AT         DATETIME(6) NULL,

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
    CONSTRAINT UK_INVITATION_TOKEN UNIQUE (TOKEN),

    CONSTRAINT FK_INVITATION_USER
        FOREIGN KEY (FK_ID_USER) REFERENCES AUT_USER (ID)
        ON DELETE CASCADE,

    CONSTRAINT FK_INVITATION_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT (ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT FK_INVITATION_ROLE
        FOREIGN KEY (FK_ID_ROLE) REFERENCES AUT_ROLE (ID)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    -- ==========================================
    -- INDEXES (SaaS Elite Tier - Atómico)
    -- ==========================================
    INDEX IDX_INVITATION_USER    (FK_ID_USER),
    INDEX IDX_INVITATION_ROLE    (FK_ID_ROLE),
    INDEX IDX_INVITATION_TENANT  (TENANT_ID),
    INDEX IDX_INVITATION_STATUS  (STATUS),
    INDEX IDX_INVITATION_EXPIRES (EXPIRES_AT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

