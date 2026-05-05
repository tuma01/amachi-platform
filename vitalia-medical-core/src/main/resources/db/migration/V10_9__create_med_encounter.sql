-- ============================================================
-- Script: V10_9__create_med_encounter.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_ENCOUNTER (SaaS Elite Tier).
--             Sustituye a MED_PATIENT_VISIT para alineación FHIR.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_ENCOUNTER (
    -- ==========================================
    -- Identity & Base Audit (AuditableTenantEntity)
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50) NOT NULL,
    TENANT_ID           BIGINT NOT NULL,
    EXTERNAL_ID         VARCHAR(36) NOT NULL UNIQUE,
    VERSION             BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1) DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Core Encounter Data
    -- ==========================================
    ENCOUNTER_DATE      DATETIME(6),
    STATUS              VARCHAR(30) NOT NULL,
    ENCOUNTER_TYPE      VARCHAR(30) NOT NULL,
    DURATION_MINUTES    INT,

    -- ==========================================
    -- Clinical Content
    -- ==========================================
    CHIEF_COMPLAINT     TEXT,
    CLINICAL_NOTES      TEXT,
    NOTES               TEXT,

    -- ==========================================
    -- Relationships (FKs)
    -- ==========================================
    FK_ID_PATIENT           BIGINT NOT NULL,
    FK_ID_DOCTOR            BIGINT,
    FK_ID_APPOINTMENT       BIGINT,
    FK_ID_EPISODEOFCARE     BIGINT,
    -- FK_ID_HISTORY        BIGINT,      -- Pendiente activar

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_ENCOUNTER_PATIENT
        FOREIGN KEY (FK_ID_PATIENT) REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_ENCOUNTER_DOCTOR
        FOREIGN KEY (FK_ID_DOCTOR) REFERENCES MED_DOCTOR(ID),

    CONSTRAINT FK_ENCOUNTER_EPISODE_OF_CARE
        FOREIGN KEY (FK_ID_EPISODEOFCARE) REFERENCES MED_EPISODE_OF_CARE(ID),

    -- CONSTRAINT FK_ENC_HISTORY
    --    FOREIGN KEY (FK_ID_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    -- CONSTRAINT FK_ENC_APPOINTMENT
    --    FOREIGN KEY (FK_ID_APPOINTMENT) REFERENCES MED_APPOINTMENT(ID),

    INDEX IDX_ENC_TENANT       (TENANT_ID),
    INDEX IDX_ENC_STATUS       (STATUS),
    INDEX IDX_ENC_PATIENT      (FK_ID_PATIENT),
    INDEX IDX_ENC_EPISODE      (FK_ID_EPISODEOFCARE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

