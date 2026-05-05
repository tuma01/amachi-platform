-- ============================================================
-- Script: V10_30__create_med_audit_tables.sql
-- Módulo: vitalia-medical-core
-- Descripción: Tablas de Auditoría (Hibernate Envers) para entidades clínicas.
--              Soporte para trazabilidad en SaaS Elite Tier.
-- ============================================================

-- 1. Tabla de Auditoría MED_PATIENT_AUD
CREATE TABLE IF NOT EXISTS MED_PATIENT_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),
    
    FK_ID_PERSON            BIGINT,
    NHC                     VARCHAR(50),
    IDENTIFICATION_NUMBER   VARCHAR(50),
    NATIONALITY             VARCHAR(100),
    EDUCATION_LEVEL         VARCHAR(100),
    OCCUPATION              VARCHAR(200),
    PREFERRED_LANGUAGE      VARCHAR(10),
    PATIENT_STATUS          VARCHAR(30),
    ALLERGY_SUMMARY         TEXT,
    ADDITIONAL_REMARKS      TEXT,
    IS_ACTIVE               TINYINT(1),
    FK_ID_BLOOD_TYPE        BIGINT,
    WEIGHT                  DECIMAL(5,2),
    HEIGHT                  DECIMAL(5,2),
    HAS_DISABILITY          TINYINT(1),
    DISABILITY_DETAILS      VARCHAR(250),
    IS_PREGNANT             TINYINT(1),
    GESTATIONAL_WEEKS       INT,
    CHILDREN_COUNT          INT,
    ETHNIC_GROUP            VARCHAR(100),
    EMERGENCY_CONTACT_NAME  VARCHAR(200),
    EMERGENCY_CONTACT_RELATION VARCHAR(50),
    EMERGENCY_CONTACT_PHONE VARCHAR(50),
    EMERGENCY_CONTACT_EMAIL VARCHAR(100),
    EMERGENCY_CONTACT_ADDRESS TEXT,
    PHOTO                   LONGBLOB,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_PATIENT_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 2. Tabla de Auditoría MED_DOCTOR_AUD
CREATE TABLE IF NOT EXISTS MED_DOCTOR_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),
    
    FK_ID_PERSON            BIGINT,
    LICENSE_NUMBER          VARCHAR(50),
    PROVIDER_NUMBER         VARCHAR(50),
    SPECIALTIES_SUMMARY     VARCHAR(255),
    BIO                     TEXT,
    CONSULTATION_PRICE      DECIMAL(12,2),
    SIGNATURE_PATH          VARCHAR(255),
    LICENSE_EXPIRY_DATE     DATE,
    HIRE_DATE               DATE,
    OFFICE_NUMBER           VARCHAR(20),
    YEARS_EXPERIENCE        INT,
    TOTAL_CONSULTATIONS     INT,
    RATING                  DOUBLE,
    IS_ACTIVE               TINYINT(1),

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_DOCTOR_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 3. Tabla de Auditoría MED_EPISODE_OF_CARE_AUD
CREATE TABLE IF NOT EXISTS MED_EPISODE_OF_CARE_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    FK_ID_PATIENT           BIGINT,
    FK_ID_MANAGING_DOCTOR   BIGINT,
    STATUS                  VARCHAR(30),
    TYPE                    VARCHAR(100),
    PERIOD_START            DATETIME(6),
    PERIOD_END              DATETIME(6),
    GOALS                   TEXT,
    NOTES                   TEXT,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_EPISODE_OF_CARE_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 4. Tabla de Auditoría MED_ENCOUNTER_AUD
CREATE TABLE IF NOT EXISTS MED_ENCOUNTER_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    ENCOUNTER_DATE          DATETIME(6),
    STATUS                  VARCHAR(30),
    ENCOUNTER_TYPE          VARCHAR(30),
    DURATION_MINUTES        INT,
    CHIEF_COMPLAINT         TEXT,
    CLINICAL_NOTES          TEXT,
    NOTES                   TEXT,
    FK_ID_PATIENT           BIGINT,
    FK_ID_DOCTOR            BIGINT,
    FK_ID_APPOINTMENT       BIGINT,
    FK_ID_EPISODEOFCARE     BIGINT,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_ENCOUNTER_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 5. Tabla de Auditoría MED_CONDITION_AUD
CREATE TABLE IF NOT EXISTS MED_CONDITION_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    FK_ID_PATIENT           BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_ICD10             BIGINT,
    FK_ID_DOCTOR            BIGINT,
    FK_ID_EPISODE_OF_CARE   BIGINT,

    DISPLAY_NAME            VARCHAR(255),
    CLINICAL_STATUS         VARCHAR(50),
    CONDITION_TYPE          VARCHAR(50),
    SEVERITY                VARCHAR(30),
    SYMPTOMS                TEXT,
    TREATMENT_NOTES         TEXT,
    DIAGNOSIS_DATE          DATE,
    ABATEMENT_DATE          DATE,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_CONDITION_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 6. Tabla de Auditoría MED_OBSERVATION_AUD
CREATE TABLE IF NOT EXISTS MED_OBSERVATION_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    FK_ID_PATIENT           BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_DOCTOR            BIGINT,
    
    STATUS                  VARCHAR(30),
    OBS_CODE                VARCHAR(100),
    DISPLAY_NAME            VARCHAR(200),
    VALUE_TEXT              TEXT,
    UNIT                    VARCHAR(30),
    REFERENCE_RANGE         VARCHAR(100),
    INTERPRETATION          VARCHAR(50),
    EFFECTIVE_DATETIME      DATETIME(6),
    NOTES                   TEXT,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_OBSERVATION_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 7. Tabla de Auditoría MED_PRESCRIPTION_AUD
CREATE TABLE IF NOT EXISTS MED_PRESCRIPTION_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    FK_ID_PATIENT           BIGINT,
    FK_ID_ENCOUNTER         BIGINT,
    FK_ID_DOCTOR            BIGINT,
    FK_ID_MEDICATION        BIGINT,

    MEDICATION_DISPLAY_NAME VARCHAR(250),
    STATUS                  VARCHAR(30),
    AUTHORED_ON             DATETIME(6),
    DOSAGE_INSTRUCTION      TEXT,
    ROUTE                   VARCHAR(50),
    FREQUENCY               VARCHAR(50),
    QUANTITY                DECIMAL(10,2),
    PRIORITY                VARCHAR(20),
    REASON_CODE             VARCHAR(100),
    NOTES                   TEXT,

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_PRESCRIPTION_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- 8. Tabla de Auditoría MED_APPOINTMENT_AUD
CREATE TABLE IF NOT EXISTS MED_APPOINTMENT_AUD (
    ID                      BIGINT NOT NULL,
    REV                     BIGINT NOT NULL,
    REVTYPE                 TINYINT,

    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    IS_DELETED              TINYINT(1),

    FK_ID_PATIENT           BIGINT,
    FK_ID_DOCTOR            BIGINT,
    FK_ID_ENCOUNTER         BIGINT,

    START_TIME              DATETIME(6),
    END_TIME                DATETIME(6),
    STATUS                  VARCHAR(30),
    SOURCE                  VARCHAR(30),
    APPOINTMENT_TYPE        VARCHAR(50),
    PRIORITY                VARCHAR(20),
    REASON                  TEXT,
    NO_SHOW                 TINYINT(1),

    CHECKED_IN_AT           DATETIME(6),
    COMPLETED_AT            DATETIME(6),
    CANCELLED_AT            DATETIME(6),
    CANCEL_REASON           TEXT,

    LOCKED_UNTIL            DATETIME(6),
    LOCKED_BY               VARCHAR(100),

    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_APPOINTMENT_AUD_REV FOREIGN KEY (REV) REFERENCES REVINFO(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

COMMIT;
