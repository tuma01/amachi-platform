-- ============================================================
-- Script: V8_1__create_mgt_avatar.sql
-- Módulo: core-management
-- Descripción: Avatar de usuario — recurso transversal de perfil (SaaS Elite Tier).
--              Vinculado a AUT_USER. Un avatar por usuario por tenant.
-- ============================================================

CREATE TABLE IF NOT EXISTS MGT_AVATAR (

    -- ==========================================
    -- Identity & Base Audit (AuditableTenantEntity)
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)   DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Relationship
    -- ==========================================
    USER_ID             BIGINT       NOT NULL,

    -- ==========================================
    -- Avatar Data
    -- ==========================================
    FILENAME            VARCHAR(255),
    MIME_TYPE           VARCHAR(100),
    CONTENT             LONGBLOB     NOT NULL,
    SIZE                BIGINT,

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints
    -- ==========================================
    CONSTRAINT FK_AVATAR_USER
        FOREIGN KEY (USER_ID) REFERENCES AUT_USER(ID) ON DELETE CASCADE,

    CONSTRAINT FK_AVATAR_TENANT
        FOREIGN KEY (TENANT_ID) REFERENCES DMN_TENANT(ID),

    CONSTRAINT UK_AVATAR_USER_TENANT
        UNIQUE (USER_ID, TENANT_ID),

    INDEX IDX_AVATAR_USER   (USER_ID),
    INDEX IDX_AVATAR_TENANT (TENANT_ID)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
