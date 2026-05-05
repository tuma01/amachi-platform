-- ============================================================
-- Script: V2_1__create_geo_country.sql
-- Módulo: core-geography
-- Descripción: Creación de la tabla GEO_COUNTRY con metadatos, auditoría y restricciones.
-- ============================================================
CREATE TABLE IF NOT EXISTS GEO_COUNTRY (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    ISO VARCHAR(2) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    NICE_NAME VARCHAR(100) NULL,
    ISO3 VARCHAR(3),
    NUM_CODE INT,
    PHONE_CODE INT NOT NULL,

    -- Concurrencia e ID Externo (Tier Elite)
    VERSION BIGINT NOT NULL DEFAULT 0,
    EXTERNAL_ID VARCHAR(36) NOT NULL UNIQUE,

    -- Auditoría
    CREATED_BY          VARCHAR(100) NOT NULL,
    CREATED_DATE        DATETIME(6)  NOT NULL,
    LAST_MODIFIED_BY    VARCHAR(100),
    LAST_MODIFIED_DATE  DATETIME(6),

    -- Constraints
    CONSTRAINT UK_COUNTRY_ISO UNIQUE (ISO)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
