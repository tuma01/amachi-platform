-- ============================================================
-- Script: V3_12__add_indexes_dmn_person.sql
-- Tabla:  DMN_PERSON
-- Motivo: SaaS Elite Tier — IS_DELETED filtra en toda búsqueda de personas
--         (entidad global, sin TENANT_ID propio).
-- ============================================================

-- Soft-delete filter (@SQLRestriction IS_DELETED = false aplicado globalmente)
CREATE INDEX IDX_PERSON_IS_DELETED
    ON DMN_PERSON (IS_DELETED);

-- Búsqueda por nombre (apellido como primer discriminante — mayor cardinalidad)
CREATE INDEX IDX_PERSON_NAME
    ON DMN_PERSON (LAST_NAME, FIRST_NAME);

COMMIT;
