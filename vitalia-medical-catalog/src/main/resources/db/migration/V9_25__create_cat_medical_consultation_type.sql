-- ============================================================
-- Script: V2_50_25__create_cat_medical_consultation_type.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Creación de la tabla CAT_MEDICAL_CONSULTATION_TYPE (SaaS Elite - Catálogo Global).
-- ============================================================

CREATE TABLE IF NOT EXISTS CAT_MEDICAL_CONSULTATION_TYPE (
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
    FK_ID_SPECIALTY         BIGINT,
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
    CONSTRAINT UK_CONS_TYPE_EXTERNAL_ID UNIQUE (EXTERNAL_ID),
    CONSTRAINT UK_CONS_TYPE_CODE UNIQUE (CODE),
    CONSTRAINT FK_MEDICAL_CONSULTATION_TYPE_SPECIALTY FOREIGN KEY (FK_ID_SPECIALTY) REFERENCES CAT_MEDICAL_SPECIALTY(ID),
    
    INDEX IDX_CONS_TYPE_CODE (CODE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
