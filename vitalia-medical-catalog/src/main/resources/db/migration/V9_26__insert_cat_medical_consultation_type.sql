-- ============================================================
-- Script: V2_50_26__insert_cat_medical_consultation_type.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_MEDICAL_CONSULTATION_TYPE (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_MEDICAL_CONSULTATION_TYPE (CODE, NAME, DESCRIPTION, EXTERNAL_ID, VERSION, CREATED_BY, CREATED_DATE) 
VALUES 
('GEN_CHECKUP', 'Consulta General / Chequeo', 'EvaluaciÃ³n mÃ©dica rutinaria y preventiva', UUID(), 0, 'SYSTEM', NOW()),
('FOLLOW_UP', 'Consulta de Seguimiento', 'Control posterior a diagnÃ³stico o tratamiento', UUID(), 0, 'SYSTEM', NOW()),
('EMERGENCY', 'Consulta de Emergencia', 'AtenciÃ³n inmediata por cuadro agudo', UUID(), 0, 'SYSTEM', NOW()),
('TELEMED', 'Telemedicina / Videoconsulta', 'Consulta remota vÃ­a plataformas digitales', UUID(), 0, 'SYSTEM', NOW()),
('SPECIALIST', 'Consulta con Especialista', 'AtenciÃ³n derivada a rama mÃ©dica especÃ­fica', UUID(), 0, 'SYSTEM', NOW()),
('HOME_VISIT', 'Visita Domiciliaria', 'AtenciÃ³n mÃ©dica en la residencia del paciente', UUID(), 0, 'SYSTEM', NOW()),
('PROCEDURE', 'Consulta para Procedimientos', 'Cita destinada a actos tÃ©cnicos o quirÃºrgicos menores', UUID(), 0, 'SYSTEM', NOW()),
('PROC_RESULT', 'Entrega de Resultados', 'Consulta para revisiÃ³n de exÃ¡menes y laboratorios', UUID(), 0, 'SYSTEM', NOW());

