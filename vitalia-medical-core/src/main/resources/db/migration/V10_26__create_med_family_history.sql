-- ============================================================
-- Script: V10_26__create_med_family_history.sql
-- Módulo: vitalia-medical-core
-- Descripción: Antecedentes patológicos familiares del expediente clínico.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_FAMILY_HISTORY (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)  NOT NULL,
    TENANT_ID               BIGINT       NOT NULL,
    EXTERNAL_ID             VARCHAR(36)  NOT NULL UNIQUE,
    VERSION                 BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED              BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_MEDICAL_HISTORY   BIGINT       NOT NULL,

    -- ==========================================
    -- Datos Familiares
    -- ==========================================
    IS_CURRENT              BOOLEAN      NOT NULL DEFAULT TRUE,
    MOTHER_HEALTH_INFO      VARCHAR(300),
    FATHER_HEALTH_INFO      VARCHAR(300),
    CARDIAC_FAMHISTORY      VARCHAR(300),
    MENTAL_FAMHISTORY       VARCHAR(300),
    DIABETES_FAMHISTORY     VARCHAR(300),
    FAMILY_ETHNICITY        VARCHAR(100),
    GENETIC_RISK_INDEX      VARCHAR(30),
    ADDITIONAL_NOTES        TEXT,

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY              VARCHAR(100) NOT NULL,
    CREATED_DATE            DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- ==========================================
    -- FK
    -- ==========================================
    CONSTRAINT FK_MED_FAM_HIST FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_FAM_HIST_TENANT  (TENANT_ID),
    INDEX IDX_FAM_HIST_HISTORY (FK_ID_MEDICAL_HISTORY)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
