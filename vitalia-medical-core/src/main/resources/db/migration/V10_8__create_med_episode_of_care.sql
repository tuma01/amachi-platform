-- ============================================================
-- Script: V10_8__create_med_episode_of_care.sql
-- Módulo: vitalia-medical
-- Descripción: Creación de la tabla MED_EPISODE_OF_CARE (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_EPISODE_OF_CARE (
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
    -- Relationships (FKs)
    -- ==========================================
    FK_ID_PATIENT           BIGINT NOT NULL,
    FK_ID_MANAGING_DOCTOR   BIGINT NOT NULL,

    -- ==========================================
    -- Episode Core Data
    -- ==========================================
    STATUS                  VARCHAR(30) NOT NULL,
    TYPE                    VARCHAR(100),
    PERIOD_START            DATETIME(6) NOT NULL,
    PERIOD_END              DATETIME(6),

    -- ==========================================
    -- Clinical Content
    -- ==========================================
    GOALS                   TEXT,
    NOTES                   TEXT,

    -- ==========================================
    -- Audit Fields
    -- ==========================================
    CREATED_BY              VARCHAR(100) NOT NULL,
    CREATED_DATE            DATETIME(6) NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- ==========================================
    -- Constraints & Indexes
    -- ==========================================
    CONSTRAINT FK_MED_EPISODE_OF_CARE_PATIENT
        FOREIGN KEY (FK_ID_PATIENT) REFERENCES MED_PATIENT(ID),

    CONSTRAINT FK_MED_EPISODE_OF_CARE_DOCTOR
        FOREIGN KEY (FK_ID_MANAGING_DOCTOR) REFERENCES MED_DOCTOR(ID),

    INDEX IDX_EPI_TENANT   (TENANT_ID),
    INDEX IDX_EPI_PATIENT  (FK_ID_PATIENT),
    INDEX IDX_EPI_STATUS   (STATUS)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

