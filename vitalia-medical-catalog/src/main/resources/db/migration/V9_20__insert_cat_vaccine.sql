-- ============================================================
-- Script: V2_50_20__insert_cat_vaccine.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_VACCINE (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_VACCINE (CODE, NAME, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('VAC-001', 'BCG (Antituberculosa)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ae295f1d-d9d7-47ba-8dbe-d16b2a0e86ea'),
('VAC-002', 'Hepatitis B (ReciÃ©n nacido/Adulto)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8b4e39e5-e836-43bc-beda-d3116a02d85a'),
('VAC-003', 'Pentavalente (DPT+HB+Hib)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0f71b7db-d150-4486-b8d7-43d66e8bd1d3'),
('VAC-004', 'Polio (Oral o Inyectable)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '65ab49ee-3a13-4eea-ad7b-d8a3066aaeaa'),
('VAC-005', 'Rotavirus', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '1cd0a47f-f1a7-4616-b8f3-435da4dddeb9'),
('VAC-006', 'Neumococo', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ae44bee5-98ee-4bf4-8eb9-ba21f5cd7709'),
('VAC-007', 'Influenza Estacional', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd90156a6-165d-4508-a493-39997ac01a9c'),
('VAC-008', 'Triple Viral (SRP - SarampiÃ³n, RubÃ©ola, Parotiditis)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '10d69e7a-1e18-4e53-a5a3-400e88cbf457'),
('VAC-009', 'Hepatitis A', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '5f2c5d7e-1a45-48b7-9707-c9ad7415ab38'),
('VAC-010', 'Varicela', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '36b75bbd-94e3-4c2e-877e-6eda7f2828ee'),
('VAC-011', 'Fiebre Amarilla', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '71d00862-1e80-4d3c-aba5-6f37118d770e'),
('VAC-012', 'VPH (Virus del Papiloma Humano)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'cbd200b2-f36e-491e-be2e-f63357c3d556'),
('VAC-013', 'DPT (Difteria, TÃ©tanos, Tos Ferina)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f64f3f39-5452-4ed3-ae05-9bc73ca7be4b'),
('VAC-014', 'Toxoide TetÃ¡nico Diferico (Td)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '17026a18-111c-4657-a3db-71150e2e7b81'),
('VAC-015', 'COVID-19 (Esquema completo)', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '18c033fd-f324-4182-b873-f91c26aa859a');

