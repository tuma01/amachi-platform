-- ============================================================
-- V10_12__create_med_room.sql
-- Tabla: MED_ROOM
-- Habitación, consultorio o box físico asignado a una unidad hospitalaria.
-- Gestiona limpieza y tipo de espacio.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_ROOM (

    -- Base: AuditableTenantEntity
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)   DEFAULT 0 NOT NULL,

    -- FK a unidad hospitalaria
    FK_ID_DEPT_UNIT     BIGINT       NOT NULL,

    -- Datos de la habitación
    ROOM_NUMBER         VARCHAR(20)  NOT NULL,
    IS_PRIVATE          TINYINT(1)   DEFAULT 0,
    ROOM_TYPE           VARCHAR(30),
    BLOCK_FLOOR         INT,
    BLOCK_CODE          VARCHAR(20),
    DESCRIPTION         TEXT,
    CLEANING_STATUS     VARCHAR(30)  DEFAULT 'CLEAN',
    IS_ACTIVE           TINYINT(1)   DEFAULT 1 NOT NULL,

    -- Auditoría
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Constraints
    CONSTRAINT UK_MED_ROOM_NUMBER_UNIT
        UNIQUE (FK_ID_DEPT_UNIT, ROOM_NUMBER),

    CONSTRAINT FK_MED_ROOM_DEPT_UNIT
        FOREIGN KEY (FK_ID_DEPT_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID),

    -- Índices
    INDEX IDX_MED_ROOM_TENANT   (TENANT_ID),
    INDEX IDX_MED_ROOM_UNIT     (FK_ID_DEPT_UNIT),
    INDEX IDX_MED_ROOM_CLEANING (CLEANING_STATUS)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
