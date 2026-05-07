-- ============================================================
-- Script: V10_32__create_med_discharge_medication.sql
-- Módulo: vitalia-medical-core
-- Descripción: Medicación prescrita en el alta hospitalaria.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_DISCHARGE_MEDICATION (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                          BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE                 VARCHAR(50)  NOT NULL,
    TENANT_ID                   BIGINT       NOT NULL,
    EXTERNAL_ID                 VARCHAR(36)  NOT NULL UNIQUE,
    VERSION                     BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED                  BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_HOSPITALIZATION       BIGINT       NOT NULL,
    FK_ID_MEDICATION            BIGINT,

    -- ==========================================
    -- Datos del Medicamento
    -- ==========================================
    MEDICATION_NAME_DISPLAY     VARCHAR(250),
    DOSAGE                      VARCHAR(100),
    FREQUENCY                   VARCHAR(100),
    DURATION                    VARCHAR(100),
    INSTRUCTIONS                TEXT,

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
    CONSTRAINT FK_MED_DIS_MED_HOSP FOREIGN KEY (FK_ID_HOSPITALIZATION) REFERENCES MED_HOSPITALIZATION(ID),
    CONSTRAINT FK_MED_DIS_MED_CAT  FOREIGN KEY (FK_ID_MEDICATION)      REFERENCES CAT_MEDICATION(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_DIS_MED_TENANT (TENANT_ID),
    INDEX IDX_DIS_MED_HOSP   (FK_ID_HOSPITALIZATION)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
