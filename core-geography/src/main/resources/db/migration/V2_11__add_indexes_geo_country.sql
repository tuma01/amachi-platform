-- ============================================================
-- Script: V2_11__add_indexes_geo_country.sql
-- Tabla:  GEO_COUNTRY
-- Motivo: SaaS Elite Tier — índices de búsqueda en catálogo de países.
--         ISO ya tiene UNIQUE (índice implícito).
--         EXTERNAL_ID ya tiene UNIQUE (índice implícito).
-- ============================================================

-- Búsqueda por nombre de país (ej: selector de país en formulario)
CREATE INDEX IDX_COUNTRY_NAME
    ON GEO_COUNTRY (NAME);

COMMIT;
