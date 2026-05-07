-- ============================================================
-- Script: V10_30__create_med_toxic_habit.sql
-- Módulo: vitalia-medical-core
-- Descripción: Hábitos tóxicos del paciente en el expediente clínico.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_TOXIC_HABIT (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)  NOT NULL,
    TENANT_ID               BIGINT       NOT NULL,
    EXTERNAL_ID             VARCHAR(36)  NOT NULL UNIQUE,
    VERSION                 BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED              BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_MEDICAL_HISTORY   BIGINT       NOT NULL,

    -- ==========================================
    -- Datos de Hábitos Tóxicos
    -- ==========================================
    IS_CURRENT              BOOLEAN      NOT NULL DEFAULT TRUE,
    ALCOHOL                 VARCHAR(50)  NOT NULL DEFAULT 'NONE',
    TOBACCO                 VARCHAR(200),
    CIGARETTES_PER_DAY      INT,
    START_AGE               INT,
    QUIT_DATE               DATE,
    DRUGS                   VARCHAR(500),
    CAFFEINE                VARCHAR(200),
    SEDENTARY_LIFESTYLE     VARCHAR(200),
    OTHERS                  VARCHAR(500),

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY              VARCHAR(100) NOT NULL,
    CREATED_DATE            DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- ==========================================
    -- FK
    -- ==========================================
    CONSTRAINT FK_MED_TOX_HAB_HIST FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_TOX_HAB_TENANT  (TENANT_ID),
    INDEX IDX_TOX_HAB_HISTORY (FK_ID_MEDICAL_HISTORY)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
