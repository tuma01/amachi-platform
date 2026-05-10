-- ============================================================
-- Script: V10_43__create_med_medication_dispense.sql
-- Módulo: vitalia-medical-core
-- Descripción: Creación de la tabla MED_MEDICATION_DISPENSE (SaaS Elite Tier).
--              Registro de dispensación de medicamento (FHIR MedicationDispense).
--              AMA-032 — cierre del flujo clínico del medicamento.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_MEDICATION_DISPENSE (

    -- ==========================================
    -- Identity & Base Audit (AuditableTenantEntity)
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)     NOT NULL,
    TENANT_ID               BIGINT          NOT NULL,
    EXTERNAL_ID             VARCHAR(36)     NOT NULL UNIQUE,
    VERSION                 BIGINT          DEFAULT 0 NOT NULL,
    IS_DELETED              TINYINT(1)      DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Relationships (FKs)
    -- ==========================================
    FK_ID_PATIENT           BIGINT          NOT NULL,
    FK_ID_PRESCRIPTION      BIGINT          NOT NULL,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_DISPENSER         BIGINT,
    FK_ID_MEDICATION        BIGINT,

    -- ==========================================
    -- MedicationDispense Core Data
    -- ==========================================
    STATUS                  VARCHAR(30)     NOT NULL,
    QUANTITY                DECIMAL(10,2),
    DAYS_SUPPLY             INT,
    LOT_NUMBER              VARCHAR(50),
    DISPENSED_AT            DATETIME(6)     NOT NULL,
    HANDED_OVER_AT          DATETIME(6),
    DISPENSER_NOTE          TEXT,

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY              VARCHAR(100)    NOT NULL,
    CREATED_DATE            DATETIME(6)     NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_MED_DISP_PATIENT
        FOREIGN KEY (FK_ID_PATIENT)      REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_MED_DISP_PRESCRIPTION
        FOREIGN KEY (FK_ID_PRESCRIPTION) REFERENCES MED_PRESCRIPTION(ID),

    CONSTRAINT FK_MED_DISP_ENCOUNTER
        FOREIGN KEY (FK_ID_ENCOUNTER)    REFERENCES MED_ENCOUNTER(ID),

    CONSTRAINT FK_MED_DISP_DISPENSER
        FOREIGN KEY (FK_ID_DISPENSER)    REFERENCES MED_DOCTOR(ID),

    CONSTRAINT FK_MED_DISP_MEDICATION
        FOREIGN KEY (FK_ID_MEDICATION)   REFERENCES CAT_MEDICATION(ID),

    INDEX IDX_MED_DISP_TENANT  (TENANT_ID),
    INDEX IDX_MED_DISP_PATIENT (FK_ID_PATIENT),
    INDEX IDX_MED_DISP_RX      (FK_ID_PRESCRIPTION)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
