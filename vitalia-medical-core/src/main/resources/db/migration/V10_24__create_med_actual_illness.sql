-- ============================================================
-- Script: V10_24__create_med_actual_illness.sql
-- Módulo: vitalia-medical-core
-- Descripción: Enfermedades actuales del expediente clínico.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_ACTUAL_ILLNESS (

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
    -- Datos Clínicos
    -- ==========================================
    DISEASE_NAME            VARCHAR(150) NOT NULL,
    DIAGNOSIS_DATE          DATE,
    SYMPTOMS                VARCHAR(500),
    TREATMENTS              VARCHAR(500),

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
    CONSTRAINT FK_MED_ACT_ILL_HIST FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_ACT_ILL_TENANT  (TENANT_ID),
    INDEX IDX_ACT_ILL_HISTORY (FK_ID_MEDICAL_HISTORY)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
