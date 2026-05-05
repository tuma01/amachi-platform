-- ============================================================
-- Script: V10_7__create_med_doctor.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_DOCTOR (SaaS Elite - Composición).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_DOCTOR (
    -- ==========================================
    -- Identity & Tenant Isolation
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50) NOT NULL,
    TENANT_ID           BIGINT NOT NULL,
    EXTERNAL_ID         VARCHAR(36) NOT NULL UNIQUE,
    VERSION             BIGINT DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1) DEFAULT 0 NOT NULL,

    -- ==========================================
    -- Core Relationships
    -- ==========================================
    FK_ID_PERSON        BIGINT NOT NULL,

    -- ==========================================
    -- Professional Information
    -- ==========================================
    LICENSE_NUMBER          VARCHAR(50) NOT NULL,
    PROVIDER_NUMBER         VARCHAR(50),
    SPECIALTIES_SUMMARY     VARCHAR(255),
    BIO                     TEXT,

    CONSULTATION_PRICE      DECIMAL(12,2),
    SIGNATURE_PATH          VARCHAR(255),
    LICENSE_EXPIRY_DATE     DATE,
    HIRE_DATE               DATE,
    OFFICE_NUMBER           VARCHAR(20),

    YEARS_EXPERIENCE        INT,
    TOTAL_CONSULTATIONS     INT DEFAULT 0,
    RATING                  DOUBLE,

    IS_ACTIVE               TINYINT(1) DEFAULT 1 NOT NULL,

    -- ==========================================
    -- Audit
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_DOCTOR_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    -- Unicidad por tenant + licencia (ajustada a tu modelo real)
    CONSTRAINT UK_MED_DOC_TENANT_LICENSE
        UNIQUE (TENANT_ID, LICENSE_NUMBER, IS_DELETED),

    INDEX IDX_DOCTOR_TENANT  (TENANT_ID),
    INDEX IDX_DOCTOR_LICENSE (LICENSE_NUMBER),
    INDEX IDX_DOCTOR_PERSON  (FK_ID_PERSON)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--CREATE TABLE IF NOT EXISTS MED_DOCTOR_PROCEDURES (
--    ID_DOCTOR      BIGINT NOT NULL,
--    PROCEDURE_NAME VARCHAR(255) NOT NULL,
--    CONSTRAINT FK_DOC_PROC_DR FOREIGN KEY (ID_DOCTOR) REFERENCES MED_DOCTOR(ID)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--
--CREATE TABLE IF NOT EXISTS MED_DOCTOR_SPECIALITY_MAP (
--    ID_DOCTOR     BIGINT NOT NULL,
--    ID_SPECIALITY BIGINT NOT NULL,
--    PRIMARY KEY (ID_DOCTOR, ID_SPECIALITY),
--    CONSTRAINT FK_DOC_SPEC_DR   FOREIGN KEY (ID_DOCTOR) REFERENCES MED_DOCTOR(ID),
--    CONSTRAINT FK_DOC_SPEC_CAT  FOREIGN KEY (ID_SPECIALITY) REFERENCES CAT_MEDICAL_SPECIALTY(ID)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

