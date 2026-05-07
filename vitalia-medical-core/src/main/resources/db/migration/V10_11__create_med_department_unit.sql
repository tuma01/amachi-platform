-- ============================================================
-- V10_11__create_med_department_unit.sql
-- Tabla: MED_DEPARTMENT_UNIT
-- Unidad organizacional hospitalaria (departamento, piso, servicio clínico).
-- Soporta jerarquía recursiva via FK_ID_PARENT_UNIT (auto-referencia).
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_DEPARTMENT_UNIT (

    -- Base: AuditableTenantEntity
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)   DEFAULT 0 NOT NULL,

    -- Datos de la unidad
    NAME                VARCHAR(150) NOT NULL,
    CODE                VARCHAR(30)  NOT NULL,
    FLOOR               VARCHAR(20),
    CONTACT_PHONE       VARCHAR(50),
    DESCRIPTION         TEXT,
    IS_ACTIVE           TINYINT(1)   DEFAULT 1 NOT NULL,
    MAX_CAPACITY        INT,
    IS_CLINICAL         TINYINT(1)   DEFAULT 1 NOT NULL,
    COST_CENTER         VARCHAR(50),

    -- Jerarquía hospitalaria (auto-referencia)
    FK_ID_PARENT_UNIT   BIGINT,

    -- Jefe de unidad — activar en Fase 2 (Employee)
    -- FK_ID_UNIT_HEAD   BIGINT,

    -- Tipo de unidad del catálogo — activar en Fase catálogos
    -- FK_ID_UNIT_TYPE   BIGINT,

    -- Auditoría
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Constraints
    CONSTRAINT UK_MED_DEPT_UNIT_CODE
        UNIQUE (TENANT_ID, CODE),

    CONSTRAINT FK_MED_DEPT_UNIT_PARENT
        FOREIGN KEY (FK_ID_PARENT_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID),

    -- Índices
    INDEX IDX_MED_DEPT_UNIT_TENANT (TENANT_ID),
    INDEX IDX_MED_DEPT_UNIT_CODE   (TENANT_ID, CODE),
    INDEX IDX_MED_DEPT_UNIT_PARENT (FK_ID_PARENT_UNIT)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
