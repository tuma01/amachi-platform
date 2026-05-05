-- ============================================================
-- Script: V3_10__create_dmn_audit_tables.sql
-- Módulo: core-domain
-- Descripción: Tablas de Auditoría para DMN_TENANT y DMN_PERSON_TENANT (Hibernate Envers).
--              Soporte para trazabilidad global en SaaS Elite Tier.
-- ============================================================

-- 1. Tabla de Auditoría DMN_TENANT_AUD
CREATE TABLE IF NOT EXISTS DMN_TENANT_AUD (
    ID                  BIGINT NOT NULL,
    REV                 BIGINT NOT NULL,
    REVTYPE             TINYINT,

    -- Identity
    tenant_code         VARCHAR(50),
    tenant_id           BIGINT,
    EXTERNAL_ID         VARCHAR(36),
    CODE                VARCHAR(100),
    NAME                VARCHAR(100),
    TYPE                VARCHAR(20),

    -- Information
    DESCRIPTION         TEXT,
    ADDRESS_ID          BIGINT,
    LOGO_URL            VARCHAR(255),
    FAVICON_URL         VARCHAR(255),
    THEME_ID            BIGINT,

    -- State
    IS_ACTIVE           TINYINT(1),
    IS_DELETED          TINYINT(1),

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_DMN_TENANT_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Tabla de Auditoría DMN_PERSON_TENANT_AUD
CREATE TABLE IF NOT EXISTS DMN_PERSON_TENANT_AUD (
    ID                  BIGINT NOT NULL,
    REV                 BIGINT NOT NULL,
    REVTYPE             TINYINT,

    -- Identity
    tenant_code         VARCHAR(50),
    tenant_id           BIGINT,
    EXTERNAL_ID         VARCHAR(36),
    FK_ID_PERSON        BIGINT,
    FK_ID_TENANT        BIGINT,
    NATIONAL_HEALTH_ID  VARCHAR(50),

    -- Role and Status
    ROLE_CONTEXT        VARCHAR(50),
    DATE_REGISTERED     DATETIME(6),
    DATE_UNREGISTERED   DATETIME(6),
    RELATION_STATUS     VARCHAR(30),

    -- State
    IS_DELETED          TINYINT(1),

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_DMN_PERSON_TENANT_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

COMMIT;
