-- ============================================================
-- Script: V2_50_12__insert_cat_blood_type.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_BLOOD_TYPE (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_BLOOD_TYPE (CODE, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('A+', 'A Positivo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '70fcfbdb-6b01-4720-a013-34191cf200bb'),
('A-', 'A Negativo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8be4211c-3d4a-46da-842f-81064b2a7bdc'),
('B+', 'B Positivo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'fc0f5333-7cca-4019-a6be-a44145018133'),
('B-', 'B Negativo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'e64aaaa3-b429-4ff6-8315-d51a43c650eb'),
('AB+', 'AB Positivo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '866d6219-5634-4895-b24c-ae75ddfd2c5b'),
('AB-', 'AB Negativo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4c285e60-acaf-40dd-b7e4-fbdaf280035c'),
('O+', 'O Positivo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c2cd9f96-7c43-4a24-92a6-1cf63da6b8fc'),
('O-', 'O Negativo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '354b5053-5a01-4ffd-9f3a-7e44c4a06bee');

