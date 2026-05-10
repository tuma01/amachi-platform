-- ============================================================
-- Script: V3_9__create_dmn_person_aud.sql
-- Módulo: core-domain
-- Descripción: Tabla de Auditoría para DMN_PERSON (Hibernate Envers).
--              Soporte para trazabilidad global en SaaS Elite Tier.
-- ============================================================

-- 1. Tabla de Revisiones de Auditoría (Hibernate Envers — extendida)
-- Registra QUIÉN hizo el cambio, CUÁNDO y en qué TENANT.
-- ISO 27001 A.12.4 / HIPAA §164.312(b) — trazabilidad completa de cambios.
CREATE TABLE IF NOT EXISTS DMN_AUDIT_REVISION (
    REV         BIGINT       AUTO_INCREMENT PRIMARY KEY,
    REVTSTMP    BIGINT       NOT NULL,
    USER_ID     BIGINT       NULL COMMENT 'ID del usuario que realizó el cambio',
    TENANT_CODE VARCHAR(50)  NULL COMMENT 'Tenant en el que ocurrió el cambio',
    USERNAME    VARCHAR(100) NULL COMMENT 'Username del actor del cambio'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Tabla de Auditoría DMN_PERSON_AUD
CREATE TABLE IF NOT EXISTS DMN_PERSON_AUD (
    -- ==========================================
    -- Identity & Revision (Envers Primary Key)
    -- ==========================================
    ID                  BIGINT NOT NULL,
    REV                 BIGINT NOT NULL,
    REVTYPE             TINYINT,

    -- ==========================================
    -- Audited Data: Identity Relationship
    -- ==========================================
    EXTERNAL_ID         VARCHAR(36),
    NATIONAL_ID         VARCHAR(100),
    NATIONAL_HEALTH_ID  VARCHAR(100),
    DOCUMENT_TYPE       VARCHAR(30),

    -- ==========================================
    -- Audited Data: Personal Information
    -- ==========================================
    FIRST_NAME          VARCHAR(50),
    MIDDLE_NAME         VARCHAR(50),
    LAST_NAME           VARCHAR(50),
    SECOND_LAST_NAME    VARCHAR(50),
    BIRTH_DATE          DATE,
    MARITAL_STATUS      VARCHAR(30),
    GENDER              VARCHAR(30),

    -- ==========================================
    -- Audited Data: Localization & Contact
    -- ==========================================
    FK_ID_ADDRESS       BIGINT,
    PHONE_NUMBER        VARCHAR(50),
    MOBILE_NUMBER       VARCHAR(50),
    EMAIL               VARCHAR(100),

    -- ==========================================
    -- Audited Data: State
    -- ==========================================
    IS_DELETED          TINYINT(1),

    -- ==========================================
    -- Constraints
    -- ==========================================
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_DMN_PERSON_AUD_REV
        FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

COMMIT;
