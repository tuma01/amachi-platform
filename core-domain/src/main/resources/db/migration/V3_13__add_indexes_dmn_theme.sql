-- ============================================================
-- Script: V3_13__add_indexes_dmn_theme.sql
-- Tabla:  DMN_THEME
-- Motivo: SaaS Elite Tier — TENANT_ID obligatorio en índices de búsqueda.
--         Cada tenant tiene su propio tema de branding.
-- ============================================================

-- Aislamiento de tenant
CREATE INDEX IDX_THEME_TENANT_ID
    ON DMN_THEME (tenant_id);

CREATE INDEX IDX_THEME_TENANT_CODE
    ON DMN_THEME (tenant_code);

-- Filtro de temas activos por tenant (query más frecuente en carga de branding)
CREATE INDEX IDX_THEME_ACTIVE
    ON DMN_THEME (tenant_id, ACTIVE, IS_DELETED);

COMMIT;
