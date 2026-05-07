-- ============================================================
-- Script: V10_35__create_med_organization.sql
-- Módulo: vitalia-medical-core
-- Descripción: Organizaciones externas o internas (Hospitales, Clínicas, Aseguradoras).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_ORGANIZATION (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Datos de la Organización
    -- ==========================================
    NAME                VARCHAR(200) NOT NULL,
    LEGAL_IDENTIFIER    VARCHAR(50),
    TYPE                VARCHAR(50),
    ADDRESS             VARCHAR(250),
    CONTACT_PHONE       VARCHAR(50),
    EMAIL               VARCHAR(150),
    WEBSITE             VARCHAR(200),
    IS_ACTIVE           BOOLEAN      NOT NULL DEFAULT TRUE,

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Unicidad
    -- ==========================================
    CONSTRAINT UK_MED_ORG_LEGAL_TENANT UNIQUE (TENANT_ID, LEGAL_IDENTIFIER),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_MED_ORG_TENANT (TENANT_ID),
    INDEX IDX_MED_ORG_TYPE   (TENANT_ID, TYPE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
