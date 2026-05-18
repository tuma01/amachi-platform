-- ============================================================
-- Script: V10_42__create_med_audit_tables_phase3.sql
-- Módulo: vitalia-medical-core
-- Descripción: Tablas de auditoría Hibernate Envers — Fase 3.
--              Entidades migradas en AMA-027 (V10_21 a V10_35).
--              ISO 27001 A.12.4 — Trazabilidad de cambios clínicos.
-- Convención: ID + REV + REVTYPE + columnas base tenant-audit + cols propias
-- ============================================================

-- -------------------------------------------------------
-- MED_APPOINTMENT_REMINDER_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_APPOINTMENT_REMINDER_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_APPOINTMENT       BIGINT,
    CHANNEL                 VARCHAR(30),
    REMINDER_STATUS         VARCHAR(50),
    SCHEDULED_DATE          TIMESTAMP NULL,
    SENT_DATE               TIMESTAMP NULL,
    READ_DATE               TIMESTAMP NULL,
    RETRY_COUNT             INT,
    TARGET                  VARCHAR(150),
    EXTERNAL_MESSAGE_ID     VARCHAR(255),
    GATEWAY_RESPONSE        TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_APPT_REMINDER_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_DOCTOR_HOSPITAL_ASSIGN_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_DOCTOR_HOSPITAL_ASSIGN_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_DOCTOR            BIGINT,
    FK_ID_HOSPITAL          BIGINT,
    START_DATE              DATE,
    END_DATE                DATE,
    IS_PRIMARY              TINYINT(1),
    ROLE_IN_HOSPITAL        VARCHAR(100),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_DHA_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_MEDICAL_HISTORY_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_MEDICAL_HISTORY_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    HISTORY_NUMBER          VARCHAR(50),
    DOCUMENT_UUID           VARCHAR(36),
    FK_ID_PATIENT           BIGINT,
    FK_ID_PERSON            BIGINT,
    FK_ID_DR_RESP           BIGINT,
    RECORD_DATE             DATE,
    VALID_UNTIL             DATE,
    IS_CURRENT              TINYINT(1),
    STATUS                  VARCHAR(30),
    CONFIDENTIALITY_LEVEL   VARCHAR(30),
    IS_ORGAN_DONOR          TINYINT(1),
    IS_LOCKED               TINYINT(1),
    OBSERVATIONS            TEXT,
    NOTES                   TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_MED_HIST_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_ACTUAL_ILLNESS_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_ACTUAL_ILLNESS_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_MEDICAL_HISTORY   BIGINT,
    DISEASE_NAME            VARCHAR(150),
    DIAGNOSIS_DATE          DATE,
    SYMPTOMS                VARCHAR(500),
    TREATMENTS              VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_ACT_ILL_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_PAST_ILLNESS_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_PAST_ILLNESS_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_HIST_MED          BIGINT,
    DISEASE_NAME            VARCHAR(150),
    DESCRIPTION             VARCHAR(500),
    SYMPTOMS                VARCHAR(500),
    TREATMENTS              VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_PAST_ILL_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_FAMILY_HISTORY_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_FAMILY_HISTORY_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_MEDICAL_HISTORY   BIGINT,
    IS_CURRENT              TINYINT(1),
    MOTHER_HEALTH_INFO      VARCHAR(300),
    FATHER_HEALTH_INFO      VARCHAR(300),
    CARDIAC_FAMHISTORY      VARCHAR(300),
    MENTAL_FAMHISTORY       VARCHAR(300),
    DIABETES_FAMHISTORY     VARCHAR(300),
    FAMILY_ETHNICITY        VARCHAR(100),
    GENETIC_RISK_INDEX      VARCHAR(30),
    ADDITIONAL_NOTES        TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_FAM_HIST_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_HEREDITARY_DISEASE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_HEREDITARY_DISEASE_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_HIST_FAM          BIGINT,
    FK_ID_KINSHIP           BIGINT,
    NAME                    VARCHAR(150),
    REMARK                  VARCHAR(500),
    DIAGNOSIS_DATE          DATE,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_HER_DIS_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_INSURANCE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_INSURANCE_AUD (
    ID                      BIGINT          NOT NULL,
    REV                     BIGINT          NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_MEDICAL_HISTORY   BIGINT,
    FK_ID_HEALTHCARE_PROVIDER BIGINT,
    POLICY_NUMBER           VARCHAR(50),
    POLICY_TYPE             VARCHAR(50),
    EFFECTIVE_DATE          DATE,
    EXPIRATION_DATE         DATE,
    COVERAGE_DETAILS        VARCHAR(1000),
    COPAY_AMOUNT            DECIMAL(12,2),
    DEDUCTIBLE_AMOUNT       DECIMAL(12,2),
    AUTH_REQUIRED           TINYINT(1),
    IS_CURRENT              TINYINT(1),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_INSURANCE_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_PHYSIOLOGICAL_HABIT_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_PHYSIOLOGICAL_HABIT_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_MEDICAL_HISTORY   BIGINT,
    IS_CURRENT              TINYINT(1),
    NUTRITION               VARCHAR(500),
    SLEEP_QUALITY           VARCHAR(30),
    SLEEP_HOURS             INT,
    URINATION               VARCHAR(250),
    DEFECATION              VARCHAR(250),
    SEXUALITY               VARCHAR(250),
    SPORTS_ACTIVITY         VARCHAR(500),
    OTHERS                  VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_PHYS_HAB_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_TOXIC_HABIT_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_TOXIC_HABIT_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_MEDICAL_HISTORY   BIGINT,
    IS_CURRENT              TINYINT(1),
    ALCOHOL                 VARCHAR(50),
    TOBACCO                 VARCHAR(200),
    CIGARETTES_PER_DAY      INT,
    START_AGE               INT,
    QUIT_DATE               DATE,
    DRUGS                   VARCHAR(500),
    CAFFEINE                VARCHAR(200),
    SEDENTARY_LIFESTYLE     VARCHAR(200),
    OTHERS                  VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_TOX_HAB_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_HOSPITALIZATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_HOSPITALIZATION_AUD (
    ID                          BIGINT          NOT NULL,
    REV                         BIGINT          NOT NULL,
    REVTYPE                     TINYINT,
    TENANT_CODE                 VARCHAR(50),
    TENANT_ID                   BIGINT,
    EXTERNAL_ID                 VARCHAR(36),
    VERSION                     BIGINT,
    IS_DELETED                  TINYINT(1),
    CREATED_BY                  VARCHAR(100),
    CREATED_DATE                DATETIME(6),
    LAST_MODIFIED_BY            VARCHAR(100),
    LAST_MODIFIED_DATE          DATETIME(6),
    FK_ID_PATIENT               BIGINT,
    FK_ID_ENCOUNTER             BIGINT,
    FK_ID_DR_RESP               BIGINT,
    FK_ID_NS_RESP               BIGINT,
    FK_ID_UNIT                  BIGINT,
    FK_ID_ROOM                  BIGINT,
    FK_ID_BED                   BIGINT,
    FK_ID_INSURANCE             BIGINT NULL,
    ADMISSION_DATE              DATETIME(6),
    DISCHARGE_DATE              DATETIME(6),
    ESTIMATED_DISCHARGE_DATE    DATETIME(6),
    FOLLOW_UP_DATE              DATE,
    HOSPITALIZATION_STATUS      VARCHAR(50),
    HOSPITALIZATION_PRIORITY    VARCHAR(30),
    DISCHARGE_STATUS            VARCHAR(30),
    ADMISSION_TYPE              VARCHAR(50),
    ADMISSION_REASON            VARCHAR(1000),
    DISCHARGE_REASON            VARCHAR(1000),
    ADMISSION_DIAGNOSIS         VARCHAR(500),
    FINAL_DIAGNOSIS             VARCHAR(500),
    TREATMENT_PLAN              TEXT,
    DIAGNOSIS_NOTES             TEXT,
    DISCHARGE_INSTRUCTIONS      TEXT,
    OBSERVATIONS                TEXT,
    INSURANCE_AUTH_NUMBER       VARCHAR(100),
    TOTAL_COST                  DECIMAL(12,2),
    CURRENCY                    VARCHAR(10),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_HOSP_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_DISCHARGE_MEDICATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_DISCHARGE_MEDICATION_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_HOSPITALIZATION   BIGINT,
    FK_ID_MEDICATION        BIGINT,
    MEDICATION_NAME_DISPLAY VARCHAR(250),
    DOSAGE                  VARCHAR(100),
    FREQUENCY               VARCHAR(100),
    DURATION                VARCHAR(100),
    INSTRUCTIONS            TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_DIS_MED_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_CONSULTATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_CONSULTATION_AUD (
    ID                          BIGINT       NOT NULL,
    REV                         BIGINT       NOT NULL,
    REVTYPE                     TINYINT,
    TENANT_CODE                 VARCHAR(50),
    TENANT_ID                   BIGINT,
    EXTERNAL_ID                 VARCHAR(36),
    VERSION                     BIGINT,
    IS_DELETED                  TINYINT(1),
    CREATED_BY                  VARCHAR(100),
    CREATED_DATE                DATETIME(6),
    LAST_MODIFIED_BY            VARCHAR(100),
    LAST_MODIFIED_DATE          DATETIME(6),
    FK_ID_PATIENT               BIGINT,
    FK_ID_DOCTOR                BIGINT,
    FK_ID_MEDICAL_HISTORY       BIGINT,
    FK_ID_CONSULTATION_TYPE     BIGINT,
    VISIT_DATETIME              DATETIME(6),
    VISIT_STATUS                VARCHAR(50),
    TRIAGE_LEVEL                VARCHAR(30),
    NOTES                       TEXT,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_CONS_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_ORGANIZATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_ORGANIZATION_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    NAME                    VARCHAR(200),
    LEGAL_IDENTIFIER        VARCHAR(50),
    TYPE                    VARCHAR(50),
    ADDRESS                 VARCHAR(250),
    CONTACT_PHONE           VARCHAR(50),
    EMAIL                   VARCHAR(150),
    WEBSITE                 VARCHAR(200),
    IS_ACTIVE               TINYINT(1),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_ORG_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_NURSE_PROF_SPECIALITY_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_NURSE_PROF_SPECIALITY_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    NAME                    VARCHAR(150),
    DESCRIPTION             VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_NURSE_SPEC_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_USER_EDUCATION_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_USER_EDUCATION_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_USERPROFILE       BIGINT,
    INSTITUTION             VARCHAR(150),
    DEGREE                  VARCHAR(150),
    START_DATE              DATE,
    END_DATE                DATE,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_EDU_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_USER_EXPERIENCE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_USER_EXPERIENCE_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_USERPROFILE       BIGINT,
    TITLE                   VARCHAR(150),
    INSTITUTION             VARCHAR(150),
    START_DATE              DATE,
    END_DATE                DATE,
    DESCRIPTION             VARCHAR(500),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_EXP_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_USER_COURSE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_USER_COURSE_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_USERPROFILE       BIGINT,
    TITLE                   VARCHAR(150),
    INSTITUTION             VARCHAR(150),
    DESCRIPTION             VARCHAR(500),
    COURSE_DATE             DATE,
    DURATION_HOURS          INT,
    CERTIFICATE_REF         VARCHAR(255),
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_COURSE_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -------------------------------------------------------
-- MED_USER_CONFERENCE_AUD
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS MED_USER_CONFERENCE_AUD (
    ID                      BIGINT       NOT NULL,
    REV                     BIGINT       NOT NULL,
    REVTYPE                 TINYINT,
    TENANT_CODE             VARCHAR(50),
    TENANT_ID               BIGINT,
    EXTERNAL_ID             VARCHAR(36),
    VERSION                 BIGINT,
    IS_DELETED              TINYINT(1),
    CREATED_BY              VARCHAR(100),
    CREATED_DATE            DATETIME(6),
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),
    FK_ID_USERPROFILE       BIGINT,
    TOPIC                   VARCHAR(150),
    DESCRIPTION             VARCHAR(500),
    ORGANIZER               VARCHAR(150),
    LOCATION                VARCHAR(255),
    IS_INTERNATIONAL        TINYINT(1),
    CONFERENCE_DATE         DATE,
    PRIMARY KEY (ID, REV),
    CONSTRAINT FK_CONF_AUD_REV FOREIGN KEY (REV) REFERENCES DMN_AUDIT_REVISION(REV)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
