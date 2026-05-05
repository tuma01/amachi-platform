-- ============================================================
-- Script: V2_12__add_indexes_geo_address.sql
-- Tabla:  GEO_ADDRESS
-- Motivo: SaaS Elite Tier — índices de búsqueda en direcciones.
--         FK_ID_COUNTRY/STATE/PROVINCE/MUNICIPALITY ya tienen índices
--         implícitos creados por InnoDB al definir los FOREIGN KEY.
-- ============================================================

-- Búsqueda por código postal (zona geográfica)
CREATE INDEX IDX_ADDRESS_ZIP_CODE
    ON GEO_ADDRESS (ZIP_CODE);

-- Búsqueda por ciudad
CREATE INDEX IDX_ADDRESS_CITY
    ON GEO_ADDRESS (CITY);

COMMIT;
