-- ============================================================
-- Script: V10_27__create_med_hereditary_disease.sql
-- Módulo: vitalia-medical-core
-- Descripción: Enfermedades hereditarias vinculadas al historial familiar.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_HEREDITARY_DISEASE (

    -- ==========================================
    -- Identidad y Auditoría Base
    -- ==========================================
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          BOOLEAN      DEFAULT FALSE NOT NULL,

    -- ==========================================
    -- Relaciones
    -- ==========================================
    FK_ID_HIST_FAM      BIGINT       NOT NULL,
    FK_ID_KINSHIP       BIGINT,

    -- ==========================================
    -- Datos Clínicos
    -- ==========================================
    NAME                VARCHAR(150) NOT NULL,
    DIAGNOSIS_DATE      DATE,
    REMARK              VARCHAR(500),

    -- ==========================================
    -- Auditoría
    -- ==========================================
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- ==========================================
    -- FK
    -- ==========================================
    CONSTRAINT FK_MED_HER_DIS_FAM FOREIGN KEY (FK_ID_HIST_FAM)  REFERENCES MED_FAMILY_HISTORY(ID),
    CONSTRAINT FK_MED_HER_DIS_KIN FOREIGN KEY (FK_ID_KINSHIP)   REFERENCES CAT_KINSHIP(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_HER_DIS_TENANT (TENANT_ID),
    INDEX IDX_HER_DIS_FAM    (FK_ID_HIST_FAM)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
