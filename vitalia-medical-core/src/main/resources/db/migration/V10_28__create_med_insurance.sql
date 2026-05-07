-- ============================================================
-- Script: V10_28__create_med_insurance.sql
-- Módulo: vitalia-medical-core
-- Descripción: Seguros médicos del paciente vinculados al expediente clínico.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_INSURANCE (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE                 VARCHAR(50)     NOT NULL,
    TENANT_ID                   BIGINT          NOT NULL,
    EXTERNAL_ID                 VARCHAR(36)     NOT NULL UNIQUE,
    VERSION                     BIGINT          DEFAULT 0 NOT NULL,
    IS_DELETED                  BOOLEAN         DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_MEDICAL_HISTORY       BIGINT          NOT NULL,
    FK_ID_HEALTHCARE_PROVIDER   BIGINT          NOT NULL,

    -- ==========================================
    -- Datos del Seguro
    -- ==========================================
    POLICY_NUMBER               VARCHAR(50),
    POLICY_TYPE                 VARCHAR(50),
    EFFECTIVE_DATE              DATE,
    EXPIRATION_DATE             DATE,
    COVERAGE_DETAILS            VARCHAR(1000),
    COPAY_AMOUNT                DECIMAL(12,2),
    DEDUCTIBLE_AMOUNT           DECIMAL(12,2),
    AUTH_REQUIRED               BOOLEAN         DEFAULT FALSE,
    IS_CURRENT                  BOOLEAN         NOT NULL DEFAULT TRUE,

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY                  VARCHAR(100) NOT NULL,
    CREATED_DATE                DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY            VARCHAR(100),
    LAST_MODIFIED_DATE          DATETIME(6),

    -- ==========================================
    -- FK
    -- ==========================================
    CONSTRAINT FK_MED_INS_HIST      FOREIGN KEY (FK_ID_MEDICAL_HISTORY)     REFERENCES MED_MEDICAL_HISTORY(ID),
    CONSTRAINT FK_MED_INS_PROVIDER  FOREIGN KEY (FK_ID_HEALTHCARE_PROVIDER) REFERENCES CAT_HEALTHCARE_PROVIDER(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_INS_TENANT   (TENANT_ID),
    INDEX IDX_INS_HISTORY  (FK_ID_MEDICAL_HISTORY),
    INDEX IDX_INS_PROVIDER (FK_ID_HEALTHCARE_PROVIDER)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
