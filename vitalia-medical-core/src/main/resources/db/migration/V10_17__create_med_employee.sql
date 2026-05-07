-- ============================================================
-- V10_17__create_med_employee.sql
-- Tabla: MED_EMPLOYEE
-- Registro laboral del personal hospitalario (staff administrativo y de soporte).
-- Vincula Identidad global (Person) con datos contractuales y de adscripción.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_EMPLOYEE (

    -- Base: AuditableTenantEntity
    ID                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE             VARCHAR(50)   NOT NULL,
    TENANT_ID               BIGINT        NOT NULL,
    EXTERNAL_ID             VARCHAR(36)   NOT NULL UNIQUE,
    VERSION                 BIGINT        DEFAULT 0 NOT NULL,
    IS_DELETED              TINYINT(1)    DEFAULT 0 NOT NULL,

    -- Identidad global (Golden Protocol)
    FK_ID_PERSON            BIGINT        NOT NULL,

    -- Referencia al usuario del sistema (ID único, sin FK JPA para evitar acoplamiento)
    USER_ID                 BIGINT        NULL
        COMMENT 'ID del usuario en AUT_USER. Referencia lógica sin FK física.',

    -- Adscripción departamental
    FK_ID_DEPT_UNIT         BIGINT        NULL,

    -- Datos laborales
    EMPLOYEE_CODE           VARCHAR(50)   NULL,
    EMPLOYEE_TYPE           VARCHAR(40)   NOT NULL
        COMMENT 'ADMINISTRATIVO, MEDICO, ENFERMERO, TECNICO, LABORATORISTA, FARMACEUTICO, OTRO',
    EMPLOYEE_STATUS         VARCHAR(30)   NOT NULL
        COMMENT 'ACTIVO, INACTIVO, SUSPENDIDO, LICENCIA, BAJA',
    PROFESSIONAL_ROLE       VARCHAR(50)   NULL
        COMMENT 'MEDICAL, NURSING, ADMINISTRATIVE, SUPPORT, GENERAL_SERVICES, LABORATORY, PHARMACY, MANAGEMENT',
    JOB_POSITION            VARCHAR(120)  NULL,
    HIRE_DATE               DATE          NULL,
    SALARY                  DECIMAL(12,2) NULL,
    EMPLOYMENT_TYPE         VARCHAR(50)   NULL
        COMMENT 'FULL_TIME, PART_TIME, CONTRACT, INTERN',
    WORK_SHIFT              VARCHAR(50)   NULL
        COMMENT 'MORNING, AFTERNOON, NIGHT, ON_CALL, ADMINISTRATIVE',
    EMERGENCY_CONTACT       VARCHAR(200)  NULL,

    -- Auditoría
    CREATED_BY              VARCHAR(100)  NOT NULL,
    CREATED_DATE            DATETIME(6)   NOT NULL,
    LAST_MODIFIED_BY        VARCHAR(100),
    LAST_MODIFIED_DATE      DATETIME(6),

    -- Constraints de unicidad (aislamiento por tenant)
    CONSTRAINT UK_EMP_TENANT_CODE
        UNIQUE (TENANT_ID, EMPLOYEE_CODE, IS_DELETED),

    CONSTRAINT UK_EMP_IDENTITY_TENANT
        UNIQUE (FK_ID_PERSON, TENANT_ID, IS_DELETED),

    -- FK
    CONSTRAINT FK_MED_EMP_PERSON
        FOREIGN KEY (FK_ID_PERSON) REFERENCES DMN_PERSON(ID),

    CONSTRAINT FK_MED_EMP_DEPT_UNIT
        FOREIGN KEY (FK_ID_DEPT_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID),

    -- Índices
    INDEX IDX_EMP_TENANT    (TENANT_ID),
    INDEX IDX_EMP_PERSON    (FK_ID_PERSON),
    INDEX IDX_EMP_CODE      (TENANT_ID, EMPLOYEE_CODE),
    INDEX IDX_EMP_DEPT_UNIT (FK_ID_DEPT_UNIT),
    INDEX IDX_EMP_STATUS    (EMPLOYEE_STATUS)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
