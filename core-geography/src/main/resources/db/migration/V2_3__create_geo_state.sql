-- ============================================================
-- Script: V2_3__create_geo_state.sql
-- Módulo: core-geography
-- Descripción: Creación de la tabla GEO_STATE con metadatos, auditoría y restricciones.
-- ============================================================
CREATE TABLE IF NOT EXISTS GEO_STATE (
    ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME VARCHAR(100) NOT NULL,
    POPULATION INT DEFAULT NULL,
    SURFACE DECIMAL(38,2) DEFAULT NULL,
    FK_ID_COUNTRY BIGINT NOT NULL,

    -- Concurrencia e ID Externo (Tier Elite)
    VERSION BIGINT NOT NULL DEFAULT 0,
    EXTERNAL_ID VARCHAR(36) NOT NULL UNIQUE,

    -- Auditoría
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Constraints
    CONSTRAINT FK_STATE_COUNTRY FOREIGN KEY (FK_ID_COUNTRY) REFERENCES GEO_COUNTRY(ID),
    UNIQUE KEY UK_STATE_NAME_COUNTRY (NAME, FK_ID_COUNTRY),
    -- Index for hierarchy validation in children
    INDEX IDX_STATE_HIERARCHY (ID, FK_ID_COUNTRY)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
