-- ============================================================
-- Script: V10_6__create_med_patient.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_PATIENT (SaaS Elite - Composición).
--              Desacoplamiento de Identidad Global y Rol Multi-tenant.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_PATIENT (
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
    -- Identity Relationship
    -- ==========================================
    FK_ID_PERSON            BIGINT NOT NULL,

    -- ==========================================
    -- Core Patient Data
    -- ==========================================
    NHC                     VARCHAR(50) NOT NULL,
    IDENTIFICATION_NUMBER   VARCHAR(50),
    NATIONALITY             VARCHAR(100),
    EDUCATION_LEVEL         VARCHAR(100),
    OCCUPATION              VARCHAR(200),
    PREFERRED_LANGUAGE      VARCHAR(10),
    PATIENT_STATUS          VARCHAR(30) NOT NULL,

    -- ==========================================
    -- Clinical & Notes
    -- ==========================================
    ALLERGY_SUMMARY         TEXT,
    ADDITIONAL_REMARKS      TEXT,
    IS_ACTIVE               TINYINT(1) DEFAULT 1 NOT NULL,

    -- ==========================================
    -- Embedded: PatientDetails
    -- ==========================================
    FK_ID_BLOOD_TYPE        BIGINT,
    WEIGHT                  DECIMAL(5,2),
    HEIGHT                  DECIMAL(5,2),
    HAS_DISABILITY          TINYINT(1) DEFAULT 0 NOT NULL,
    DISABILITY_DETAILS      VARCHAR(250),
    IS_PREGNANT             TINYINT(1) DEFAULT 0 NOT NULL,
    GESTATIONAL_WEEKS       INT,
    CHILDREN_COUNT          INT,
    ETHNIC_GROUP            VARCHAR(100),

    -- ==========================================
    -- Emergency Contact
    -- ==========================================
    EMERGENCY_CONTACT_NAME      VARCHAR(200),
    EMERGENCY_CONTACT_RELATION  VARCHAR(50),
    EMERGENCY_CONTACT_PHONE     VARCHAR(50),
    EMERGENCY_CONTACT_EMAIL     VARCHAR(100),
    EMERGENCY_CONTACT_ADDRESS   TEXT,

    -- ==========================================
    -- Binary Data
    -- ==========================================
    -- FK_ID_HISTORY         BIGINT,      -- Pendiente activar
    PHOTO                  LONGBLOB,

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY             VARCHAR(100) NOT NULL,
    CREATED_DATE           DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY       VARCHAR(100),
    LAST_MODIFIED_DATE     DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_PATIENT_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    CONSTRAINT FK_PATIENT_BLOOD_TYPE
        FOREIGN KEY (FK_ID_BLOOD_TYPE) REFERENCES CAT_BLOOD_TYPE(ID),

    -- CONSTRAINT FK_MED_PAT_HIST
    --    FOREIGN KEY (FK_ID_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    CONSTRAINT UK_MED_PAT_IDENTITY_TENANT
        UNIQUE (FK_ID_PERSON, TENANT_ID, IS_DELETED),

    CONSTRAINT UK_MED_PAT_TENANT_NHC
        UNIQUE (TENANT_ID, NHC, IS_DELETED),

    INDEX IDX_PATIENT_TENANT (TENANT_ID),
    INDEX IDX_PATIENT_NHC    (NHC),
    INDEX IDX_PATIENT_PERSON (FK_ID_PERSON)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
