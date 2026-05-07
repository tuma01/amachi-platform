-- ============================================================
-- V10_18__create_med_nurse.sql
-- Tabla: MED_NURSE
-- Perfil del personal de enfermería. Vincula identidad global (Person)
-- con datos asistenciales: licencia, rango, turno y habilidades clínicas.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_NURSE (

    -- Base: AuditableTenantEntity
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)   NOT NULL,
    TENANT_ID               BIGINT        NOT NULL,
    EXTERNAL_ID             VARCHAR(36)   NOT NULL UNIQUE,
    VERSION                 BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED              TINYINT(1)    DEFAULT 0 NOT NULL,

    -- Identidad global (Golden Protocol)
    FK_ID_PERSON            BIGINT        NOT NULL,

    -- Referencia al usuario del sistema (lógica, sin FK física)
    USER_ID                 BIGINT        NULL
        COMMENT 'ID del usuario en AUT_USER.',

    -- Adscripción
    FK_ID_DEPT_UNIT         BIGINT        NULL,

    -- Perfil curricular (OneToOne, Nurse es propietario de FK)
    FK_ID_USERPROFILE       BIGINT        NULL,

    -- Datos de enfermería
    NURSE_LICENSE           VARCHAR(100)  NULL,
    NURSE_RANK              VARCHAR(100)  NULL
        COMMENT 'LICENCIADA, AUXILIAR, JEFE_ENFERMERIA, ESPECIALISTA',
    WORK_SHIFT              VARCHAR(50)   NULL
        COMMENT 'MORNING, AFTERNOON, NIGHT, ON_CALL, ADMINISTRATIVE',
    LICENSE_EXPIRY          DATE          NULL,
    HIRE_DATE               DATE          NULL,
    CONTRACT_TYPE           VARCHAR(50)   NULL,
    EMERGENCY_CONTACT       VARCHAR(200)  NULL,
    PHOTO                   LONGBLOB      NULL,

    -- Auditoría
    CREATED_BY              VARCHAR(100)  NOT NULL,
    CREATED_DATE            DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- Constraints de unicidad
    CONSTRAINT UK_NURSE_TENANT_LICENSE
        UNIQUE (TENANT_ID, NURSE_LICENSE, IS_DELETED),

    CONSTRAINT UK_NURSE_IDENTITY_TENANT
        UNIQUE (FK_ID_PERSON, TENANT_ID, IS_DELETED),

    -- FK
    CONSTRAINT FK_MED_NURSE_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    CONSTRAINT FK_MED_NURSE_DEPT_UNIT
        FOREIGN KEY (FK_ID_DEPT_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID),

    CONSTRAINT FK_MED_NURSE_USERPROFILE
        FOREIGN KEY (FK_ID_USERPROFILE) REFERENCES MED_USER_PROFILE(ID),

    -- Índices
    INDEX IDX_NURSE_TENANT    (TENANT_ID),
    INDEX IDX_NURSE_PERSON    (FK_ID_PERSON),
    INDEX IDX_NURSE_LICENSE   (TENANT_ID, NURSE_LICENSE),
    INDEX IDX_NURSE_DEPT_UNIT (FK_ID_DEPT_UNIT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_NURSE_SKILLS (ElementCollection de habilidades clínicas)
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_NURSE_SKILLS (
    ID_NURSE    BIGINT       NOT NULL,
    SKILL       VARCHAR(255) NOT NULL,

    CONSTRAINT FK_MED_NURSE_SKILLS_NURSE
        FOREIGN KEY (ID_NURSE) REFERENCES MED_NURSE(ID) ON DELETE CASCADE,

    INDEX IDX_NURSE_SKILLS (ID_NURSE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_NURSE_PROF_SPECIALITY
-- Catálogo tenant-scoped de especialidades de enfermería.
-- Es un catálogo propio del hospital, independiente del catálogo global.
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_NURSE_PROF_SPECIALITY (

    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    NAME                VARCHAR(150)  NOT NULL,
    DESCRIPTION         VARCHAR(500),

    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    CONSTRAINT UK_NPS_TENANT_NAME
        UNIQUE (TENANT_ID, NAME),

    INDEX IDX_NPS_TENANT (TENANT_ID),
    INDEX IDX_NPS_NAME   (NAME)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
