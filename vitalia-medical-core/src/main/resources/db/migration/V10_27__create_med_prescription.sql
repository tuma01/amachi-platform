-- ============================================================
-- Script: V10_27__create_med_prescription.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_PRESCRIPTION (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_PRESCRIPTION (
    -- ==========================================
    -- Identity & Base Audit (AuditableTenantEntity)
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50) NOT NULL,
    TENANT_ID               BIGINT NOT NULL,
    EXTERNAL_ID             VARCHAR(36) NOT NULL UNIQUE,
    VERSION                 BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED              TINYINT(1) DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Relationships (FKs)
    -- ==========================================
    FK_ID_PATIENT           BIGINT NOT NULL,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_DOCTOR            BIGINT NOT NULL,
    FK_ID_MEDICATION        BIGINT,

    -- ==========================================
    -- Prescription Core Data
    -- ==========================================
    MEDICATION_DISPLAY_NAME VARCHAR(250),
    STATUS                  VARCHAR(30) NOT NULL,
    AUTHORED_ON             DATETIME(6) NOT NULL,
    DOSAGE_INSTRUCTION      TEXT,
    ROUTE                   VARCHAR(50),
    FREQUENCY               VARCHAR(50),
    QUANTITY                DECIMAL(10,2),
    PRIORITY                VARCHAR(20),
    REASON_CODE             VARCHAR(100),
    NOTES                   TEXT,

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
    CONSTRAINT FK_PRESCRIPTION_PATIENT
        FOREIGN KEY (FK_ID_PATIENT) REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_PRESCRIPTION_ENCOUNTER
        FOREIGN KEY (FK_ID_ENCOUNTER) REFERENCES MED_ENCOUNTER(ID),

    CONSTRAINT FK_PRESCRIPTION_DOCTOR
        FOREIGN KEY (FK_ID_DOCTOR) REFERENCES MED_DOCTOR(ID),

    CONSTRAINT FK_PRESCRIPTION_MEDICATION
        FOREIGN KEY (FK_ID_MEDICATION) REFERENCES CAT_MEDICATION(ID),

    INDEX IDX_PRESCRIPTION_TENANT  (TENANT_ID),
    INDEX IDX_PRESCRIPTION_PATIENT (FK_ID_PATIENT),
    INDEX IDX_PRESCRIPTION_DOC     (FK_ID_DOCTOR)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;