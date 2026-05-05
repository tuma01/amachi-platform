-- ============================================================
-- Script: V2_50_05__create_cat_medication.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Creación de la tabla CAT_MEDICATION (SaaS Elite - Catálogo Global).
-- ============================================================

CREATE TABLE IF NOT EXISTS CAT_MEDICATION (
    -- ==========================================
    -- Identity & Base Audit (AuditableEntity)
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    EXTERNAL_ID             VARCHAR(36) NOT NULL UNIQUE,
    VERSION                 BIGINT DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Catalog Data
    -- ==========================================
    CODE                    VARCHAR(50) NOT NULL,
    GENERIC_NAME            VARCHAR(250) NOT NULL,
    COMMERCIAL_NAME         VARCHAR(250),
    CONCENTRATION           VARCHAR(100),
    PHARMACEUTICAL_FORM     VARCHAR(100),
    PRESENTATION            VARCHAR(250),
    DESCRIPTION             TEXT,
    IS_ACTIVE               TINYINT(1) DEFAULT 1 NOT NULL,

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY              VARCHAR(100) NOT NULL,
    CREATED_DATE            DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT UK_MEDICATION_EXTERNAL_ID UNIQUE (EXTERNAL_ID),
    CONSTRAINT UK_MEDICATION_CODE UNIQUE (CODE),
    
    INDEX IDX_MEDICATION_CODE (CODE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
