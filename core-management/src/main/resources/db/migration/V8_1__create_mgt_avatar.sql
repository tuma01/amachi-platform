-- Script: V8_1__create_mgt_avatar.sql
-- Módulo: vitalia-management
-- Descripción: Creación de la tabla MGT_AVATAR con metadatos, auditoría y restricciones.

-- ============================================
-- Creación de la tabla MGT_AVATAR
-- ============================================
CREATE TABLE IF NOT EXISTS MGT_AVATAR (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    tenant_code VARCHAR(50) NOT NULL, tenant_id BIGINT NOT NULL,
    EXTERNAL_ID VARCHAR(36) UNIQUE,
    VERSION BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED TINYINT(1) DEFAULT 0 NOT NULL,
    USER_ID BIGINT NOT NULL,
    FILENAME VARCHAR(255),
    MIME_TYPE VARCHAR(50),
    CONTENT LONGBLOB NOT NULL,
    SIZE BIGINT,
    -- ===============================
    -- Auditoría
    -- ===============================
    CREATED_BY VARCHAR(100),
    CREATED_DATE DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY VARCHAR(100),
    LAST_MODIFIED_DATE DATETIME(6),

    -- ===============================
    -- Restricciones de unicidad
    -- ===============================
    CONSTRAINT FK_AVATAR_USER FOREIGN KEY (USER_ID) REFERENCES AUT_USER(ID) ON DELETE CASCADE,
    CONSTRAINT FK_AVATAR_TENANT FOREIGN KEY (tenant_id) REFERENCES DMN_TENANT(ID) ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Índices
-- ============================================
CREATE UNIQUE INDEX IDX_AVATAR_USER_TENANT ON MGT_AVATAR(USER_ID, tenant_id);
CREATE INDEX IDX_AVATAR_USER ON MGT_AVATAR(USER_ID);
CREATE INDEX IDX_AVATAR_TENANT ON MGT_AVATAR(tenant_id);

