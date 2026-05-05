-- ============================================================
-- Script: V10_22__create_med_condition.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_CONDITION (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_CONDITION (
    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE VARCHAR(50) NOT NULL,
    TENANT_ID BIGINT NOT NULL,
    EXTERNAL_ID         VARCHAR(36) NOT NULL UNIQUE,
    VERSION             BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED          BOOLEAN DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones (Contexto Clínico)
    -- ==========================================
    FK_ID_PATIENT           BIGINT NOT NULL,
    -- FK_ID_MEDICAL_HISTORY   BIGINT NOT NULL,          -- Pendiente activar
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_ICD10             BIGINT,                      -- Referencia al catálogo global de diagnósticos
    FK_ID_DOCTOR            BIGINT NOT NULL,             -- Médico que diagnóstica
    FK_ID_EPISODE_OF_CARE   BIGINT,

    -- ==========================================
    -- Datos de la Condición (FHIR)
    -- ==========================================
    DISPLAY_NAME        VARCHAR(255),                    -- Nombre legible del diagnóstico
    CLINICAL_STATUS     VARCHAR(50) NOT NULL,            -- ACTIVE, RECURRENCE, RELAPSE, INACTIVE, REMISSION, RESOLVED
    CONDITION_TYPE      VARCHAR(50) NOT NULL,            -- PROBLEM, DIAGNOSIS
    SEVERITY            VARCHAR(30),                     -- MILD, MODERATE, SEVERE
    SYMPTOMS            TEXT,
    TREATMENT_NOTES     TEXT,
    DIAGNOSIS_DATE      DATE,
    ABATEMENT_DATE      DATE,

    -- ==========================================
    -- Auditoría de Operación
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_CONDITION_PATIENT FOREIGN KEY (FK_ID_PATIENT) REFERENCES MED_PATIENT(ID),
    -- CONSTRAINT FK_CONDITION_HIST    FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),
    CONSTRAINT FK_CONDITION_ENCOUNTER     FOREIGN KEY (FK_ID_ENCOUNTER) REFERENCES MED_ENCOUNTER(ID),
    CONSTRAINT FK_CONDITION_DOCTOR      FOREIGN KEY (FK_ID_DOCTOR) REFERENCES MED_DOCTOR(ID),
    CONSTRAINT FK_CONDITION_ICD10     FOREIGN KEY (FK_ID_ICD10) REFERENCES CAT_ICD10(ID),
    CONSTRAINT FK_CONDITION_EPISODE_OF_CARE     FOREIGN KEY (FK_ID_EPISODE_OF_CARE) REFERENCES MED_EPISODE_OF_CARE(ID),

    INDEX IDX_COND_TENANT  (TENANT_ID),
    INDEX IDX_COND_PATIENT (FK_ID_PATIENT),
    INDEX IDX_COND_ICD10   (FK_ID_ICD10)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

