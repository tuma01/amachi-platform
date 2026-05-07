-- ============================================================
-- Script: V10_33__create_med_consultation.sql
-- Módulo: vitalia-medical-core
-- Descripción: Registro de consultas médicas (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_CONSULTATION (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE                 VARCHAR(50)  NOT NULL,
    TENANT_ID                   BIGINT       NOT NULL,
    EXTERNAL_ID                 VARCHAR(36)  NOT NULL UNIQUE,
    VERSION                     BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED                  BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_PATIENT               BIGINT       NOT NULL,
    FK_ID_DOCTOR                BIGINT,
    FK_ID_MEDICAL_HISTORY       BIGINT       NOT NULL,
    FK_ID_CONSULTATION_TYPE     BIGINT,

    -- ==========================================
    -- Datos de la Consulta
    -- ==========================================
    VISIT_DATETIME              DATETIME(6)  NOT NULL,
    VISIT_STATUS                VARCHAR(50)  NOT NULL DEFAULT 'SCHEDULED',
    TRIAGE_LEVEL                VARCHAR(30),
    NOTES                       TEXT,

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
    CONSTRAINT FK_MED_CONS_PATIENT  FOREIGN KEY (FK_ID_PATIENT)           REFERENCES MED_PATIENT(ID),
    CONSTRAINT FK_MED_CONS_DOCTOR   FOREIGN KEY (FK_ID_DOCTOR)            REFERENCES MED_DOCTOR(ID),
    CONSTRAINT FK_MED_CONS_HISTORY  FOREIGN KEY (FK_ID_MEDICAL_HISTORY)   REFERENCES MED_MEDICAL_HISTORY(ID),
    CONSTRAINT FK_MED_CONS_TYPE     FOREIGN KEY (FK_ID_CONSULTATION_TYPE) REFERENCES CAT_MEDICAL_CONSULTATION_TYPE(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_CONS_TENANT  (TENANT_ID),
    INDEX IDX_CONS_PATIENT (FK_ID_PATIENT),
    INDEX IDX_CONS_DATE    (VISIT_DATETIME)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
