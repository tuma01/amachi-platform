-- ============================================================
-- Script: V2_50_24__insert_cat_medical_unit_type.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_MEDICAL_UNIT_TYPE (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_MEDICAL_UNIT_TYPE (CODE, NAME, DESCRIPTION, EXTERNAL_ID, VERSION, CREATED_BY, CREATED_DATE) 
VALUES 
('ICU_ADULT', 'Unidad de Cuidados Intensivos Adultos', 'Unidad especializada para pacientes adultos crÃ­ticos', UUID(), 0, 'SYSTEM', NOW()),
('ICU_NEO', 'Unidad de Cuidados Intensivos Neonatales', 'Unidad para reciÃ©n nacidos de alto riesgo', UUID(), 0, 'SYSTEM', NOW()),
('OR', 'QuirÃ³fano / Sala de Operaciones', 'Ãrea destinada a procedimientos quirÃºrgicos', UUID(), 0, 'SYSTEM', NOW()),
('EMERGENCY', 'Departamento de Emergencias', 'AtenciÃ³n inmediata y tripartizaje', UUID(), 0, 'SYSTEM', NOW()),
('PEDIATRICS', 'Unidad de PediatrÃ­a', 'AtenciÃ³n especializada para niÃ±os y adolescentes', UUID(), 0, 'SYSTEM', NOW()),
('CARDIOLOGY', 'Unidad de CardiologÃ­a', 'Servicio especializado en salud cardiovascular', UUID(), 0, 'SYSTEM', NOW()),
('MATERNITY', 'Unidad de Maternidad', 'AtenciÃ³n a embarazadas y obstetricia', UUID(), 0, 'SYSTEM', NOW()),
('RECOVERY', 'Sala de RecuperaciÃ³n', 'Ãrea post-anestÃ©sica y post-quirÃºrgica', UUID(), 0, 'SYSTEM', NOW()),
('DIALYSIS', 'Unidad de DiÃ¡lisis', 'Tratamiento de insuficiencia renal', UUID(), 0, 'SYSTEM', NOW()),
('ONCOLOGY', 'Unidad de OncologÃ­a', 'Tratamiento y seguimiento de pacientes con cÃ¡ncer', UUID(), 0, 'SYSTEM', NOW());

