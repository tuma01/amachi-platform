-- ============================================================
-- Script: V4_8__create_aut_user_tenant_role.sql
-- Módulo: core-auth
-- Descripción: Tabla para asignar roles de seguridad a usuarios en un Tenant específico.
-- Posee aislamiento nativo multi-tenant (SaaS Elite Tier).
-- ============================================================

create TABLE IF NOT EXISTS AUT_USER_TENANT_ROLE (

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
    FK_ID_ROLE          BIGINT NOT NULL,

    -- ==========================================
    -- Datos de Asignación
    -- ==========================================
    ASSIGNED_AT         DATETIME(6) NOT NULL,
    REVOKED_AT          DATETIME(6),
    ACTIVE              BOOLEAN DEFAULT TRUE NOT NULL,

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
    -- IMPORTANTE: incluir IS_DELETED para soft delete en el Unique Constraint
    CONSTRAINT UK_USER_TENANT_ROLE
        UNIQUE (FK_ID_USER, TENANT_ID, FK_ID_ROLE, IS_DELETED),

    CONSTRAINT FK_USER_TENANT_ROLE_USER
        FOREIGN KEY (FK_ID_USER) REFERENCES AUT_USER (ID),

    CONSTRAINT FK_USER_TENANT_ROLE_ROLE
        FOREIGN KEY (FK_ID_ROLE) REFERENCES AUT_ROLE (ID),

    CONSTRAINT FK_USER_TENANT_ROLE_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT (ID),

    -- ==========================================
    -- Índices (SaaS Elite Tier - Atómico)
    -- ==========================================
    INDEX IDX_UTR_TENANT   (TENANT_ID),
    INDEX IDX_UTR_USER     (FK_ID_USER),
    INDEX IDX_UTR_ROLE     (FK_ID_ROLE),
    INDEX IDX_UTR_ACTIVE   (ACTIVE),
    INDEX IDX_UTR_ASSIGNED (ASSIGNED_AT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



