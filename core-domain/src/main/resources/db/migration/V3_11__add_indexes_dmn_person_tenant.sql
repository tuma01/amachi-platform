-- ============================================================
-- Script: V3_11__add_indexes_dmn_person_tenant.sql
-- Tabla:  DMN_PERSON_TENANT
-- Motivo: SaaS Elite Tier — TENANT_ID obligatorio en índices de búsqueda.
--         Sin estos índices, cada query multi-tenant hace full table scan.
-- ============================================================

-- Aislamiento de tenant (activado por Hibernate tenantFilter)
CREATE INDEX IDX_PERSON_TENANT_TENANT_ID
    ON DMN_PERSON_TENANT (tenant_id);

CREATE INDEX IDX_PERSON_TENANT_TENANT_CODE
    ON DMN_PERSON_TENANT (tenant_code);

-- Soft-delete filter (@SQLRestriction IS_DELETED = false)
CREATE INDEX IDX_PERSON_TENANT_IS_DELETED
    ON DMN_PERSON_TENANT (IS_DELETED);

-- Join desde DMN_PERSON hacia sus contextos de tenant
CREATE INDEX IDX_PERSON_TENANT_FK_PERSON
    ON DMN_PERSON_TENANT (FK_ID_PERSON);

-- Índice compuesto: patrón de query más frecuente —
-- "dame los contextos activos de este tenant"
CREATE INDEX IDX_PERSON_TENANT_SEARCH
    ON DMN_PERSON_TENANT (tenant_id, IS_DELETED, RELATION_STATUS);

COMMIT;
