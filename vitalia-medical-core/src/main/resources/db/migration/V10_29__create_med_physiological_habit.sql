-- ============================================================
-- Script: V10_29__create_med_physiological_habit.sql
-- Módulo: vitalia-medical-core
-- Descripción: Hábitos fisiológicos del paciente en el expediente clínico.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_PHYSIOLOGICAL_HABIT (

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
    -- Datos Fisiológicos
    -- ==========================================
    IS_CURRENT              BOOLEAN      NOT NULL DEFAULT TRUE,
    NUTRITION               VARCHAR(500),
    SLEEP_QUALITY           VARCHAR(30),
    SLEEP_HOURS             INT,
    URINATION               VARCHAR(250),
    DEFECATION              VARCHAR(250),
    SEXUALITY               VARCHAR(250),
    SPORTS_ACTIVITY         VARCHAR(500),
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
    CONSTRAINT FK_MED_PHYS_HAB_HIST FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_HAB_PHYS_TENANT  (TENANT_ID),
    INDEX IDX_HAB_PHYS_HISTORY (FK_ID_MEDICAL_HISTORY)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
