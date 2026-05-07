-- ============================================================
-- V10_19__create_med_professional_info.sql
-- Tabla: MED_PROFESSIONAL_INFO
-- Trayectoria laboral del profesional de salud por período.
-- Registra cargo, posición, departamento y contexto de rol en cada organización.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_PROFESSIONAL_INFO (

    -- Base: AuditableTenantEntity
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)   NOT NULL,
    TENANT_ID               BIGINT        NOT NULL,
    EXTERNAL_ID             VARCHAR(36)   NOT NULL UNIQUE,
    VERSION                 BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED              TINYINT(1)    DEFAULT 0 NOT NULL,

    -- Identidad del profesional
    FK_ID_PERSON            BIGINT        NOT NULL,

    -- Organización — referencia lógica sin FK física (Organization no existe aún)
    ORGANIZATION_ID         BIGINT        NULL
        COMMENT 'ID de la organización (MED_ORGANIZATION). FK lógica, sin constraint hasta Fase 6.',

    -- Período
    PERIOD_START_DATE       DATE          NOT NULL,
    PERIOD_END_DATE         DATE          NULL,
    IS_CURRENT              TINYINT(1)    DEFAULT 0 NOT NULL,

    -- Contexto profesional
    ROLE_CONTEXT            VARCHAR(50)   NOT NULL
        COMMENT 'MEDICAL, NURSING, ADMINISTRATIVE, SUPPORT, GENERAL_SERVICES, LABORATORY, PHARMACY, MANAGEMENT',
    POSITION                VARCHAR(100)  NULL,
    DEPARTMENT              VARCHAR(100)  NULL,
    EMPLOYEE_ID_REF         VARCHAR(50)   NULL
        COMMENT 'Código de empleado en la organización externa',
    LICENSE_NUMBER          VARCHAR(100)  NULL,
    EXPERIENCE_AT_START     INT           NULL,
    CONTRACT_TYPE           VARCHAR(50)   NULL,
    WORK_SCHEDULE           VARCHAR(100)  NULL,
    SALARY_GRADE            VARCHAR(50)   NULL,
    SUPERVISOR              VARCHAR(100)  NULL,
    PERFORMANCE_RATING      DECIMAL(3,1)  NULL,
    NOTES                   TEXT          NULL,

    -- Auditoría
    CREATED_BY              VARCHAR(100)  NOT NULL,
    CREATED_DATE            DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- FK
    CONSTRAINT FK_MED_PROF_INFO_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    -- Índices
    INDEX IDX_PROF_INFO_TENANT  (TENANT_ID),
    INDEX IDX_PROF_INFO_PERSON  (FK_ID_PERSON),
    INDEX IDX_PROF_INFO_CURRENT (IS_CURRENT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
