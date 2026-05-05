-- ============================================================
-- Script: V10_23__create_med_observation.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_OBSERVATION (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_OBSERVATION (
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
    -- Relationships (FKs)
    -- ==========================================
    FK_ID_PATIENT       BIGINT NOT NULL,
    FK_ID_ENCOUNTER     BIGINT,
    FK_ID_DOCTOR        BIGINT NOT NULL,

    -- ==========================================
    -- Observation Core Data
    -- ==========================================
    STATUS              VARCHAR(30) NOT NULL,
    OBS_CODE            VARCHAR(100) NOT NULL,
    DISPLAY_NAME        VARCHAR(200),
    VALUE_TEXT          TEXT,
    UNIT                VARCHAR(30),
    REFERENCE_RANGE     VARCHAR(100),
    INTERPRETATION      VARCHAR(50),
    EFFECTIVE_DATETIME  DATETIME(6) NOT NULL,

    -- ==========================================
    -- Additional Content
    -- ==========================================
    NOTES               TEXT,

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
    CONSTRAINT FK_OBSERVATION_PATIENT
        FOREIGN KEY (FK_ID_PATIENT) REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_OBSERVATION_ENCOUNTER
        FOREIGN KEY (FK_ID_ENCOUNTER) REFERENCES MED_ENCOUNTER(ID),

    CONSTRAINT FK_OBSERVATION_DOCTOR
        FOREIGN KEY (FK_ID_DOCTOR) REFERENCES MED_DOCTOR(ID),

    INDEX IDX_OBS_TENANT   (TENANT_ID),
    INDEX IDX_OBS_PATIENT  (FK_ID_PATIENT),
    INDEX IDX_OBS_CODE     (OBS_CODE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
