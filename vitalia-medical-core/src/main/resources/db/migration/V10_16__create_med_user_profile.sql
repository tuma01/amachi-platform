-- ============================================================
-- V10_16__create_med_user_profile.sql
-- Tabla: MED_USER_PROFILE
-- Perfil curricular extendido del profesional de salud.
-- Contenedor de formación académica, experiencia, cursos y congresos.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_USER_PROFILE (

    -- Base: AuditableTenantEntity
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    -- Contenido curricular
    BIOGRAPHY           VARCHAR(2000),
    PHOTO               LONGBLOB,

    -- Auditoría
    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Índices
    INDEX IDX_USER_PROFILE_TENANT (TENANT_ID)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_USER_EDUCATION
-- Formación académica del profesional.
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_USER_EDUCATION (

    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    FK_ID_USERPROFILE   BIGINT        NOT NULL,

    INSTITUTION         VARCHAR(150)  NOT NULL,
    DEGREE              VARCHAR(150)  NOT NULL,
    START_DATE          DATE          NOT NULL,
    END_DATE            DATE,

    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    CONSTRAINT FK_MED_EDUCATION_PROFILE
        FOREIGN KEY (FK_ID_USERPROFILE) REFERENCES MED_USER_PROFILE(ID),

    INDEX IDX_USER_EDU_TENANT  (TENANT_ID),
    INDEX IDX_USER_EDU_PROFILE (FK_ID_USERPROFILE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_USER_EXPERIENCE
-- Trayectoria laboral previa del profesional.
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_USER_EXPERIENCE (

    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    FK_ID_USERPROFILE   BIGINT        NOT NULL,

    TITLE               VARCHAR(150)  NOT NULL,
    INSTITUTION         VARCHAR(150)  NOT NULL,
    START_DATE          DATE          NOT NULL,
    END_DATE            DATE,
    DESCRIPTION         VARCHAR(500),

    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    CONSTRAINT FK_MED_EXPERIENCE_PROFILE
        FOREIGN KEY (FK_ID_USERPROFILE) REFERENCES MED_USER_PROFILE(ID),

    INDEX IDX_USER_EXP_TENANT  (TENANT_ID),
    INDEX IDX_USER_EXP_PROFILE (FK_ID_USERPROFILE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_USER_COURSE
-- Cursos, diplomados y capacitaciones del profesional.
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_USER_COURSE (

    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    FK_ID_USERPROFILE   BIGINT        NOT NULL,

    TITLE               VARCHAR(150)  NOT NULL,
    INSTITUTION         VARCHAR(150)  NOT NULL,
    DESCRIPTION         VARCHAR(500),
    COURSE_DATE         DATE          NOT NULL,
    DURATION_HOURS      INT           NOT NULL,
    CERTIFICATE_REF     VARCHAR(255),

    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    CONSTRAINT FK_MED_COURSE_PROFILE
        FOREIGN KEY (FK_ID_USERPROFILE) REFERENCES MED_USER_PROFILE(ID),

    INDEX IDX_USER_CRS_TENANT  (TENANT_ID),
    INDEX IDX_USER_CRS_PROFILE (FK_ID_USERPROFILE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- Sub-tabla: MED_USER_CONFERENCE
-- Participación en congresos y eventos científicos.
-- ============================================================
CREATE TABLE IF NOT EXISTS MED_USER_CONFERENCE (

    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)   NOT NULL,
    TENANT_ID           BIGINT        NOT NULL,
    EXTERNAL_ID         VARCHAR(36)   NOT NULL UNIQUE,
    VERSION             BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)    DEFAULT 0 NOT NULL,

    FK_ID_USERPROFILE   BIGINT        NOT NULL,

    TOPIC               VARCHAR(150)  NOT NULL,
    DESCRIPTION         VARCHAR(500),
    ORGANIZER           VARCHAR(150),
    LOCATION            VARCHAR(255),
    IS_INTERNATIONAL    TINYINT(1)    DEFAULT 0 NOT NULL,
    CONFERENCE_DATE     DATE          NOT NULL,

    CREATED_BY          VARCHAR(100)  NOT NULL,
    CREATED_DATE        DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    CONSTRAINT FK_MED_CONFERENCE_PROFILE
        FOREIGN KEY (FK_ID_USERPROFILE) REFERENCES MED_USER_PROFILE(ID),

    INDEX IDX_USER_CONF_TENANT  (TENANT_ID),
    INDEX IDX_USER_CONF_PROFILE (FK_ID_USERPROFILE)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
