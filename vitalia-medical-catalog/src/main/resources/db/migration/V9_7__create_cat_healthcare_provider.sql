-- ============================================================
-- Script: V2_50_07__create_cat_healthcare_provider.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Creación de la tabla CAT_HEALTHCARE_PROVIDER (SaaS Elite - Catálogo Global).
-- ============================================================

CREATE TABLE IF NOT EXISTS CAT_HEALTHCARE_PROVIDER (
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
    NAME                    VARCHAR(250) NOT NULL,
    TAX_ID                  VARCHAR(50) NOT NULL,
    OFFICIAL_EMAIL          VARCHAR(100),
    OFFICIAL_PHONE          VARCHAR(50),
    EMERGENCY_PHONE         VARCHAR(50),
    WEBSITE                 VARCHAR(155),
    LOGO_URL                VARCHAR(255),
    FK_ID_HQ_ADDRESS        BIGINT,
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
    CONSTRAINT UK_PROVIDER_EXTERNAL_ID UNIQUE (EXTERNAL_ID),
    CONSTRAINT UK_PROVIDER_CODE UNIQUE (CODE),
    CONSTRAINT UK_PROVIDER_TAX_ID UNIQUE (TAX_ID),
    CONSTRAINT FK_PROVIDER_HQ_ADDRESS FOREIGN KEY (FK_ID_HQ_ADDRESS) REFERENCES GEO_ADDRESS(ID),
    
    INDEX IDX_PROVIDER_NAME (NAME)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
