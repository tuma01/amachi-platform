-- ============================================================
-- Script: V2_50_22__insert_cat_demographic.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_DEMOGRAPHIC (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_GENDER (CODE, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('M', 'Masculino', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a4797f29-5215-4a5b-b5dc-8da0c64d7bfd'),
('F', 'Femenino', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '99721d2d-6005-48c0-8165-119423e33027'),
('NB', 'No Binario', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'dd860dbd-48a6-4468-ab34-3136732a23ad'),
('UNK', 'Desconocido', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ef3b2af2-e074-4af2-ac5d-da21f8e33459'),
('OTH', 'Otro', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'da01f6c5-9cdb-4654-8179-468ac394fab2');

INSERT INTO CAT_CIVIL_STATUS(CODE, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('SINGLE', 'Soltero/a', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '60731560-7593-4366-ac7b-fb19602f03d2'),
('MARRIED', 'Casado/a', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2d2f6c97-d5fb-4944-b6d4-a2b07d2b5103'),
('DIVORCED', 'Divorciado/a', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2d51fb72-8672-4b0f-a970-b9fd2d33a807'),
('WIDOWED', 'Viudo/a', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '47336f7f-c042-4157-8f27-c4a72f1881ef'),
('UNION', 'UniÃ³n Libre', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ea904868-0794-4867-bc92-8a8777f0c39c'),
('NONE', 'No Reportado', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ed8ee40f-27d3-4773-96ee-dd17434cf1fb');

