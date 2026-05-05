-- ============================================================
-- Script: V2_50_09__create_cat_medical_specialty.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Creación de la tabla CAT_MEDICAL_SPECIALTY (SaaS Elite - Catálogo Global).
-- ============================================================

CREATE TABLE IF NOT EXISTS CAT_MEDICAL_SPECIALTY (
    -- ==========================================
    -- Identity & Base Audit (AuditableEntity)
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    EXTERNAL_ID             VARCHAR(36) NOT NULL UNIQUE,
    VERSION                 BIGINT DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Catalog Data
    -- ==========================================
    CODE                    VARCHAR(20) NOT NULL,
    NAME                    VARCHAR(150) NOT NULL,
    DESCRIPTION             VARCHAR(500),
    TARGET_PROFESSION       VARCHAR(20) DEFAULT 'BOTH',
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
    CONSTRAINT UK_SPECIALTY_EXTERNAL_ID UNIQUE (EXTERNAL_ID),
    CONSTRAINT UK_SPECIALTY_CODE UNIQUE (CODE),
    CONSTRAINT CHK_SPECIALTY_TARGET CHECK (TARGET_PROFESSION IN ('DOCTOR', 'NURSE', 'BOTH')),
    
    INDEX IDX_SPECIALTY_CODE (CODE),
    INDEX IDX_SPECIALTY_NAME (NAME)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
