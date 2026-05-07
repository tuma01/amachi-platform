-- ============================================================
-- V10_13__create_med_bed.sql
-- Tabla: MED_BED
-- Cama hospitalaria individual dentro de una habitación.
-- Unidad mínima de ocupación. Controla disponibilidad y mantenimiento.
-- ============================================================

CREATE TABLE IF NOT EXISTS MED_BED (

    -- Base: AuditableTenantEntity
    ID                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    TENANT_CODE         VARCHAR(50)  NOT NULL,
    TENANT_ID           BIGINT       NOT NULL,
    EXTERNAL_ID         VARCHAR(36)  NOT NULL UNIQUE,
    VERSION             BIGINT       DEFAULT 0 NOT NULL,
    IS_DELETED          TINYINT(1)   DEFAULT 0 NOT NULL,

    -- FK a habitación
    FK_ID_ROOM          BIGINT       NOT NULL,

    -- Datos de la cama
    BED_NUMBER          VARCHAR(20)  NOT NULL,
    BED_CODE            VARCHAR(30)  NOT NULL,
    IS_OCCUPIED         TINYINT(1)   DEFAULT 0 NOT NULL,
    STATUS              VARCHAR(30)  DEFAULT 'AVAILABLE',
    DESCRIPTION         TEXT,
    MAINTENANCE_DUE     DATE,
    IS_ACTIVE           TINYINT(1)   DEFAULT 1 NOT NULL,

    -- Auditoría
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Constraints
    CONSTRAINT UK_MED_BED_CODE_ROOM
        UNIQUE (FK_ID_ROOM, BED_CODE),

    CONSTRAINT FK_MED_BED_ROOM
        FOREIGN KEY (FK_ID_ROOM) REFERENCES MED_ROOM(ID),

    -- Índices
    INDEX IDX_MED_BED_TENANT (TENANT_ID),
    INDEX IDX_MED_BED_ROOM   (FK_ID_ROOM),
    INDEX IDX_MED_BED_STATUS (STATUS)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
