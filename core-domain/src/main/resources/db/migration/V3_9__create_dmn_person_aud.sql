-- ============================================================
-- Script: V3_9__create_dmn_person_aud.sql
-- Módulo: core-domain
-- Descripción: Tabla de Auditoría para DMN_PERSON (Hibernate Envers).
--              Soporte para trazabilidad global en SaaS Elite Tier.
-- ============================================================

-- 1. Tabla de Revisiones Globales (Standard Envers)
CREATE TABLE IF NOT EXISTS REVINFO (
    REV BIGINT AUTO_INCREMENT PRIMARY KEY,
    REVTSTMP BIGINT
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
        FOREIGN KEY (REV) REFERENCES REVINFO(REV)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

COMMIT;
