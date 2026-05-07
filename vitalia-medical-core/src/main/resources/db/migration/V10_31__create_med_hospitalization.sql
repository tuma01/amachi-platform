-- ============================================================
-- Script: V10_31__create_med_hospitalization.sql
-- Módulo: vitalia-medical-core
-- Descripción: Episodios de internación hospitalaria (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_HOSPITALIZATION (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE                 VARCHAR(50)     NOT NULL,
    TENANT_ID                   BIGINT          NOT NULL,
    EXTERNAL_ID                 VARCHAR(36)     NOT NULL UNIQUE,
    VERSION                     BIGINT          DEFAULT 0 NOT NULL,
    IS_DELETED                  BOOLEAN         DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_PATIENT               BIGINT          NOT NULL,
    FK_ID_ENCOUNTER             BIGINT,
    FK_ID_DR_RESP               BIGINT,
    FK_ID_NS_RESP               BIGINT,
    FK_ID_UNIT                  BIGINT,
    FK_ID_ROOM                  BIGINT,
    FK_ID_BED                   BIGINT,

    -- ==========================================
    -- Fechas del Episodio
    -- ==========================================
    ADMISSION_DATE              DATETIME(6)     NOT NULL,
    DISCHARGE_DATE              DATETIME(6),
    ESTIMATED_DISCHARGE_DATE    DATETIME(6),
    FOLLOW_UP_DATE              DATE,

    -- ==========================================
    -- Estado y Clasificación
    -- ==========================================
    HOSPITALIZATION_STATUS      VARCHAR(50)     NOT NULL,
    HOSPITALIZATION_PRIORITY    VARCHAR(30),
    DISCHARGE_STATUS            VARCHAR(30),
    ADMISSION_TYPE              VARCHAR(50),

    -- ==========================================
    -- Datos Clínicos
    -- ==========================================
    ADMISSION_REASON            VARCHAR(1000),
    DISCHARGE_REASON            VARCHAR(1000),
    ADMISSION_DIAGNOSIS         VARCHAR(500),
    FINAL_DIAGNOSIS             VARCHAR(500),
    TREATMENT_PLAN              TEXT,
    DIAGNOSIS_NOTES             TEXT,
    DISCHARGE_INSTRUCTIONS      TEXT,
    OBSERVATIONS                TEXT,

    -- ==========================================
    -- Facturación
    -- ==========================================
    INSURANCE_AUTH_NUMBER       VARCHAR(100),
    TOTAL_COST                  DECIMAL(12,2),
    CURRENCY                    VARCHAR(10)     DEFAULT 'USD',

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
    CONSTRAINT FK_MED_HOSP_PATIENT   FOREIGN KEY (FK_ID_PATIENT)   REFERENCES MED_PATIENT(ID),
    CONSTRAINT FK_MED_HOSP_ENCOUNTER FOREIGN KEY (FK_ID_ENCOUNTER) REFERENCES MED_ENCOUNTER(ID),
    CONSTRAINT FK_MED_HOSP_DR_RESP   FOREIGN KEY (FK_ID_DR_RESP)   REFERENCES MED_DOCTOR(ID),
    CONSTRAINT FK_MED_HOSP_NS_RESP   FOREIGN KEY (FK_ID_NS_RESP)   REFERENCES MED_NURSE(ID),
    CONSTRAINT FK_MED_HOSP_UNIT      FOREIGN KEY (FK_ID_UNIT)      REFERENCES MED_DEPARTMENT_UNIT(ID),
    CONSTRAINT FK_MED_HOSP_ROOM      FOREIGN KEY (FK_ID_ROOM)      REFERENCES MED_ROOM(ID),
    CONSTRAINT FK_MED_HOSP_BED       FOREIGN KEY (FK_ID_BED)       REFERENCES MED_BED(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_HOSP_TENANT  (TENANT_ID),
    INDEX IDX_HOSP_PATIENT (FK_ID_PATIENT),
    INDEX IDX_HOSP_STATUS  (HOSPITALIZATION_STATUS),
    INDEX IDX_HOSP_DATE    (ADMISSION_DATE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
