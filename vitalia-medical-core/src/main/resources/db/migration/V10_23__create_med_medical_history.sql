-- ============================================================
-- Script: V10_23__create_med_medical_history.sql
-- Módulo: vitalia-medical-core
-- Descripción: Expediente clínico longitudinal del paciente (EHR / SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_MEDICAL_HISTORY (

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
    FK_ID_PATIENT           BIGINT       NOT NULL,
    FK_ID_PERSON            BIGINT,
    FK_ID_DR_RESP           BIGINT,

    -- ==========================================
    -- Identificación del Expediente
    -- ==========================================
    HISTORY_NUMBER          VARCHAR(50)  NOT NULL,
    DOCUMENT_UUID           VARCHAR(36),

    -- ==========================================
    -- Ciclo de Vida
    -- ==========================================
    RECORD_DATE             DATE         NOT NULL,
    VALID_UNTIL             DATE,
    IS_CURRENT              BOOLEAN      NOT NULL DEFAULT TRUE,
    STATUS                  VARCHAR(30)  NOT NULL DEFAULT 'ACTIVE',
    CONFIDENTIALITY_LEVEL   VARCHAR(30)  DEFAULT 'NORMAL',
    IS_ORGAN_DONOR          BOOLEAN      NOT NULL DEFAULT FALSE,
    IS_LOCKED               BOOLEAN      NOT NULL DEFAULT FALSE,

    -- ==========================================
    -- Notas Clínicas
    -- ==========================================
    OBSERVATIONS            TEXT,
    NOTES                   TEXT,

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
    CONSTRAINT FK_MED_HIST_PATIENT  FOREIGN KEY (FK_ID_PATIENT)  REFERENCES MED_PATIENT(ID),
    CONSTRAINT FK_MED_HIST_PERSON   FOREIGN KEY (FK_ID_PERSON)   REFERENCES DMN_PERSON(ID),
    CONSTRAINT FK_MED_HIST_DR_RESP  FOREIGN KEY (FK_ID_DR_RESP)  REFERENCES MED_DOCTOR(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_HIST_TENANT  (TENANT_ID),
    INDEX IDX_HIST_PATIENT (FK_ID_PATIENT),
    INDEX IDX_HIST_NUMBER  (HISTORY_NUMBER)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
