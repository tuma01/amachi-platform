-- ============================================================
-- Script: V10_21__create_med_appointment_reminder.sql
-- Módulo: vitalia-medical-core
-- Descripción: Recordatorios de citas médicas (SaaS Elite Tier).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_APPOINTMENT_REMINDER (

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
    FK_ID_APPOINTMENT       BIGINT       NOT NULL,

    -- ==========================================
    -- Datos del Recordatorio
    -- ==========================================
    CHANNEL                 VARCHAR(30)  NOT NULL,
    REMINDER_STATUS         VARCHAR(50)  NOT NULL,
    SCHEDULED_DATE          DATETIME(6)  NOT NULL,
    SENT_DATE               DATETIME(6),
    READ_DATE               DATETIME(6),
    RETRY_COUNT             INT          DEFAULT 0,
    TARGET                  VARCHAR(150),
    EXTERNAL_MESSAGE_ID     VARCHAR(255),
    GATEWAY_RESPONSE        TEXT,

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
    CONSTRAINT FK_MED_REMINDER_APPOINTMENT FOREIGN KEY (FK_ID_APPOINTMENT) REFERENCES MED_APPOINTMENT(ID),

    -- ==========================================
    -- INDEXES
    -- ==========================================
    INDEX IDX_APP_REM_TENANT (TENANT_ID),
    INDEX IDX_APP_REM_APPT   (FK_ID_APPOINTMENT),
    INDEX IDX_APP_REM_STATUS (REMINDER_STATUS)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
