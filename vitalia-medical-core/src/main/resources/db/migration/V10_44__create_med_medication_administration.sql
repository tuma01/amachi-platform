-- ============================================================
-- Script: V10_44__create_med_medication_administration.sql
-- Módulo: vitalia-medical-core
-- Descripción: Creación de la tabla MED_MEDICATION_ADMINISTRATION (SaaS Elite Tier).
--              Registro de administración de dosis al paciente (FHIR MedicationAdministration).
--              AMA-032 — kardex de enfermería / trazabilidad de dosis administradas.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_MEDICATION_ADMINISTRATION (

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
    FK_ID_DISPENSE          BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_NURSE             BIGINT,

    -- ==========================================
    -- MedicationAdministration Core Data
    -- ==========================================
    STATUS                  VARCHAR(30)     NOT NULL,
    ADMINISTERED_AT         DATETIME(6)     NOT NULL,
    DOSE_QUANTITY           DECIMAL(10,2),
    DOSE_UNIT               VARCHAR(30),
    ROUTE                   VARCHAR(50),
    NOTES                   TEXT,

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
    CONSTRAINT FK_MED_ADM_PATIENT
        FOREIGN KEY (FK_ID_PATIENT)      REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_MED_ADM_PRESCRIPTION
        FOREIGN KEY (FK_ID_PRESCRIPTION) REFERENCES MED_PRESCRIPTION(ID),

    CONSTRAINT FK_MED_ADM_DISPENSE
        FOREIGN KEY (FK_ID_DISPENSE)     REFERENCES MED_MEDICATION_DISPENSE(ID),

    CONSTRAINT FK_MED_ADM_ENCOUNTER
        FOREIGN KEY (FK_ID_ENCOUNTER)    REFERENCES MED_ENCOUNTER(ID),

    CONSTRAINT FK_MED_ADM_NURSE
        FOREIGN KEY (FK_ID_NURSE)        REFERENCES MED_NURSE(ID),

    INDEX IDX_MED_ADM_TENANT  (TENANT_ID),
    INDEX IDX_MED_ADM_PATIENT (FK_ID_PATIENT),
    INDEX IDX_MED_ADM_RX      (FK_ID_PRESCRIPTION)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
