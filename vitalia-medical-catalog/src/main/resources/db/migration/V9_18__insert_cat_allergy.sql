-- ============================================================
-- Script: V2_50_18__insert_cat_allergy.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_ALLERGY (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_ALLERGY (CODE, NAME, TYPE, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES

-- Medicamentos
('ALL-001', 'Penicilina y derivados', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b2fe7f88-b0c1-4588-80f5-701638fb93e5'),
('ALL-002', 'Sulfonamidas (Sulfas)', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '28aabe64-1eb4-4703-aadc-0151db84a693'),
('ALL-003', 'Aspirina y AINEs (Ibuprofeno, Naproxeno)', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '90abed41-1e1c-4bee-917b-a19116e22d6c'),
('ALL-004', 'Anticonvulsivantes', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f104978b-030f-40fa-8deb-8ee0698f3f9f'),
('ALL-005', 'Insulina', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '5b0eb29f-7d37-480e-83dc-5a5c19f34da0'),
('ALL-006', 'Medios de contraste yodados', 'DRUG', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2afa6080-1049-468f-ae4f-24cab56a0720'),

-- Alimentos
('ALL-100', 'ProteÃ­na de leche de vaca', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f0e2b49c-f9bd-4b0b-b2ef-38ae0525d38d'),
('ALL-101', 'Huevo', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '7ac01267-8ef8-4ad8-97b7-b06925e522da'),
('ALL-102', 'ManÃ­ (Cacahuetes)', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '69d5b025-a1d2-460c-baba-5ec69e06f685'),
('ALL-103', 'Frutos secos (Nueces, Almendras)', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'aa2b0e60-643f-487f-a449-0a71a2fcb4ab'),
('ALL-104', 'Pescados y Mariscos', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2b4389a2-5d4b-4346-988f-efc02dccbccd'),
('ALL-105', 'Trigo (Gluten)', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'be7f7fe8-358b-467e-adea-7e752f6ceae8'),
('ALL-106', 'Soya', 'FOOD', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a1e07d6e-55bc-47d2-9655-6c0012c87a07'),

-- Ambiental / Otros
('ALL-200', 'Polen', 'ENVIRONMENTAL', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3a16a146-6c8a-44c9-b1a9-3825e249b273'),
('ALL-201', 'Ãcaros del polvo', 'ENVIRONMENTAL', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ffa57735-dcfa-47aa-a962-c5569eb7c0f6'),
('ALL-202', 'Epitelio de animales (Gato, Perro)', 'ENVIRONMENTAL', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '27e70630-4069-4133-9ed8-93e23441b572'),
('ALL-203', 'LÃ¡tex', 'OTHER', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ceb2c8bd-a560-4dea-9d48-60e42855fbbc'),
('ALL-204', 'Picaduras de insectos (Abeja, Avispa)', 'ENVIRONMENTAL', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c3679f70-849e-483a-846a-ee50449f6345');

