-- ============================================================
-- Script  : V9_27__insert_cat_healthcare_provider_bolivia.sql
-- Módulo  : vitalia-medical-catalog
-- País    : Bolivia (BO)
-- Desc.   : Aseguradoras y cajas de salud bolivianas más comunes.
--           Se usa INSERT IGNORE para garantizar idempotencia.
-- ============================================================

INSERT IGNORE INTO CAT_HEALTHCARE_PROVIDER
    (CODE, NAME, TAX_ID, OFFICIAL_EMAIL, OFFICIAL_PHONE, EMERGENCY_PHONE, WEBSITE, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID)
VALUES

-- ── Cajas de Salud públicas ───────────────────────────────────────────────
('CNS-BOL',
 'Caja Nacional de Salud (CNS)',
 '1014543020',
 'info@cns.gob.bo', '2222-777', '2222-778',
 'https://www.cns.gob.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('COSSMIL-BOL',
 'Corporación del Seguro Social Militar (COSSMIL)',
 '1014543021',
 'contacto@cossmil.mil.bo', '2442-001', '2442-002',
 'https://www.cossmil.mil.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('CBSE-BOL',
 'Caja de Salud de la Banca Privada (CBSE)',
 '1014543022',
 'atencion@cbse.bo', '2203-500', NULL,
 'https://www.cbse.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('CAMOC-BOL',
 'Caja de Salud de Caminos y Ramas Anexas (CAMOC)',
 '1014543023',
 'servicios@camoc.gob.bo', '2441-500', NULL,
 NULL,
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('CORDES-BOL',
 'Caja de Salud CORDES',
 '1014543024',
 'informaciones@cordes.bo', '2278-000', NULL,
 NULL,
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('FERROVIA-BOL',
 'Caja de Salud Ferroviaria',
 '1014543025',
 'servicios@caja-ferroviaria.bo', '2369-000', NULL,
 NULL,
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

-- ── Seguros privados ──────────────────────────────────────────────────────
('BOLCIACRUZ-BOL',
 'La Boliviana Ciacruz Seguros',
 '1020543010',
 'salud@bolivianaciacruz.com', '2310-2020', '800-100-200',
 'https://www.bolivianaciacruz.com',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('ALIANZA-BOL',
 'Alianza Seguros y Reaseguros',
 '1020543011',
 'siniestros@alianzaseguros.bo', '2311-5000', '800-107-700',
 'https://www.alianzaseguros.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('BUPA-BOL',
 'BUPA Bolivia Seguros',
 '1020543012',
 'salud@bupa.com.bo', '2277-2600', '800-100-BUPA',
 'https://www.bupa.com.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('ZURICH-BOL',
 'Zurich Bolivia Seguros Personales',
 '1020543013',
 'atencion@zurich.com.bo', '2310-3500', NULL,
 'https://www.zurich.com.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

('ILLIMANI-BOL',
 'Illimani Seguros',
 '1020543014',
 'seguros@illimani.bo', '2202-4400', NULL,
 NULL,
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

-- ── Sistemas públicos universales ────────────────────────────────────────
('SUS-BOL',
 'Sistema Único de Salud (SUS)',
 '1000000001',
 'sus@minsalud.gob.bo', '800-10-6111', NULL,
 'https://www.minsalud.gob.bo',
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID()),

-- ── Pago particular ──────────────────────────────────────────────────────
('PARTICULAR-BOL',
 'Particular / Sin Aseguradora',
 '0000000000',
 NULL, NULL, NULL, NULL,
 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, UUID());
