-- ============================================================
-- Script: V2_50_14__insert_cat_kinship.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_KINSHIP (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_KINSHIP (CODE, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('FATHER', 'Padre', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'e9875224-a6a6-47af-bc1c-e34ee546327c'),
('MOTHER', 'Madre', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4eb7e27e-a109-45d1-9b5a-87e70dbbaa1f'),
('SPOUSE', 'CÃ³nyuge / CompaÃ±ero(a)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8d1a412f-9dd5-4c8c-ab60-cc573e74821c'),
('SON', 'Hijo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ef199b09-6cb6-44f9-8f82-37d5f6fcf09d'),
('DAUGHTER', 'Hija', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'cac3a5c7-46b3-4cd8-b56e-25c52c4667a5'),
('BROTHER', 'Hermano', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'aaebba33-aa25-4abf-8605-4d5cb994c4a8'),
('SISTER', 'Hermana', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '5e8e746e-cbd0-4109-af67-5c86cbafe0a7'),
('GRANDFATHER', 'Abuelo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0c39172d-de18-4386-82c5-1a90d961a54b'),
('GRANDMOTHER', 'Abuela', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '98a5e1b3-9095-423d-ae33-5967ea245997'),
('UNCLE', 'TÃ­o', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f6476934-dda8-4d77-b3a0-a418495b5928'),
('AUNT', 'TÃ­a', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '11c4abf0-097e-4634-964f-d37d428ae451'),
('COUSIN', 'Primo / Prima', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '507f8f81-246c-4dd6-b305-c3a1ccd64a32'),
('OTHER', 'Otro', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '22c50c02-d8fb-49e9-b3ab-d5d2eec501b8'),
('NONE', 'Ninguno', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd8701e5d-0032-4fc6-98d4-bb4c4d60c6a1');

