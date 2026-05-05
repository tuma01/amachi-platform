-- ============================================================
-- Script: V2_50_03__create_cat_medical_procedure.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Creación de la tabla CAT_MEDICAL_PROCEDURE (SaaS Elite - Catálogo Global).
-- ============================================================

CREATE TABLE IF NOT EXISTS CAT_MEDICAL_PROCEDURE (
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
    NAME                    VARCHAR(500) NOT NULL,
    TYPE                    VARCHAR(50) NOT NULL, -- e.g., LABORATORY, RADIOLOGY, SURGERY
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
    CONSTRAINT UK_PROCEDURE_EXTERNAL_ID UNIQUE (EXTERNAL_ID),
    CONSTRAINT UK_PROCEDURE_CODE UNIQUE (CODE),
    
    INDEX IDX_PROCEDURE_CODE (CODE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
