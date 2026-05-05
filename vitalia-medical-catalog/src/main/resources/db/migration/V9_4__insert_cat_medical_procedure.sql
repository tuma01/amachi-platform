-- ============================================================
-- Script: V2_50_04__insert_cat_medical_procedure.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_MEDICAL_PROCEDURE (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_MEDICAL_PROCEDURE (CODE, NAME, TYPE, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES

-- Laboratorio ClÃ­nico
('LAB-001', 'Hemograma completo', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c6695aff-83b2-4432-aa9a-1686b5c8f0b3'),
('LAB-002', 'Glucosa en sangre', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4461b2f2-1a44-4abd-a4f1-7245972715cc'),
('LAB-003', 'Creatinina en suero', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4eb928f4-c253-430c-9026-5618585ed2ff'),
('LAB-004', 'UrianÃ¡lisis completo', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '6f6a8d95-3fb4-49c2-b67d-8a8705105146'),
('LAB-005', 'Perfil lipÃ­dico (Colesterol, LDL, HDL, TriglicÃ©ridos)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '10f5fe14-5135-481b-8c50-3581874d2aba'),
('LAB-006', 'Prueba de embarazo (HCG)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4ff8c49e-d06e-49f6-a7a2-c2a25add49c6'),
('LAB-007', 'Hemoglobina glicosilada (HbA1c)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'e2f38181-4f22-4687-92c4-5624d8298ac3'),
('LAB-008', 'Electrolitos (Sodio, Potasio, Cloro)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'cbfa7212-653d-41a2-9e27-938c719fdb00'),
('LAB-009', 'Tiempo de protrombina (PT)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'de7adf2a-02ac-4e1e-998f-3b4d12e60d8f'),
('LAB-010', 'Transaminasa oxalacÃ©tica (ASAT-GOT)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0c1d5f6c-bed4-4a28-a814-92bde5262c58'),
('LAB-011', 'Transaminasa pirÃºvica (ALAT-GPT)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '1b3f3c52-597c-4120-80f3-ca5e0fbf1248'),
('LAB-012', 'ProteÃ­na C Reactiva (PCR)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f8c73daa-0fbf-49f2-acfa-57331ca39766'),
('LAB-013', 'Perfil Tiroideo (TSH, T3, T4)', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2b6f987c-aba8-4083-b039-02de4ffd1fc0'),
('LAB-014', 'Ãcido Ãšrico en suero', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '92d7f6e5-9cc4-494d-9066-bac942c75657'),
('LAB-015', 'CoprolÃ³gico por montaje directo', 'LABORATORY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a1a1faa1-f932-4355-8a7a-fb4d7205239e'),

-- ImÃ¡genes diagnÃ³sticas
('IMG-001', 'RadiografÃ­a de tÃ³rax (PA y Lateral)', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2c6004a2-1adf-4d7b-baa9-e49325004e74'),
('IMG-002', 'EcografÃ­a abdominal total', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f21e24d5-586d-43da-a988-f12a4d03dd42'),
('IMG-003', 'TomografÃ­a computarizada (TC) de crÃ¡neo simple', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9340a2fd-07f5-4646-a84e-6f6b2801e012'),
('IMG-004', 'Resonancia nuclear magnÃ©tica (RNM) de columna lumbar', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'aef48b26-aabd-4e89-af50-4dcea3f80751'),
('IMG-005', 'MamografÃ­a bilateral', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ceb80a8b-c125-4874-a1ac-c539d982994f'),
('IMG-006', 'Electrocardiograma de reposo (EKG)', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '5e1107d4-b590-4619-93ca-eca6a122e5b3'),
('IMG-007', 'Ecocardiograma transtorÃ¡cico', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '71e93e9d-51bc-4960-af5b-a9ea3b84eec3'),
('IMG-008', 'DensitometrÃ­a Ã³sea', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3b9041a3-0884-4693-aed2-c7d9416758d2'),
('IMG-009', 'EcografÃ­a obstÃ©trica', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3a97f9d9-db36-41aa-bc33-dcea6680d0fb'),
('IMG-010', 'TC de Abdomen y Pelvis con contraste', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b633e16d-77b8-4a3c-9c6e-38ae6b07cf7b'),
('IMG-011', 'EcografÃ­a de tejidos blandos', 'RADIOLOGY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ef256678-0010-46f9-875c-e49c5b40ed11'),

-- Procedimientos QuirÃºrgicos y Otros
('SUR-001', 'ApendicectomÃ­a simple', 'SURGERY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4fbfbba8-474a-4e31-b6c8-2fa8c2c8e12d'),
('SUR-002', 'ColecistectomÃ­a por laparoscopia', 'SURGERY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0b7899c6-99d4-4e18-9f4e-dbef744b5ef2'),
('SUR-003', 'Herniorrafia inguinal simple', 'SURGERY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '700d69c7-85b2-49ce-9cf5-dcde5530be3a'),
('SUR-004', 'CesÃ¡rea segmentaria', 'SURGERY', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '7273abcf-299d-4400-b403-2dcfbf61dc2b'),
('SUR-005', 'Consulta de medicina general', 'CONSULTATION', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b2da5b4e-5ccf-4404-9abe-bddad0f6f9f5'),
('SUR-006', 'Consulta especializada (Primera vez)', 'CONSULTATION', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a3ab2d43-7d37-44ec-8a2c-0c89356c758c'),
('SUR-007', 'CuraciÃ³n de herida simple', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9912d8b6-c2c3-429e-94f6-338bc9398ee5'),
('SUR-008', 'Sutura de herida en piel', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4b8ffbab-7613-407a-9927-84b90d52a72d'),
('SUR-009', 'Cateterismo cardÃ­aco izquierdo', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f2d9a0a7-d581-4c3d-bb16-db41809748a8'),
('SUR-010', 'Endoscopia digestiva superior', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '65a974e3-3499-49df-89e3-bd47bf4c397d'),
('SUR-011', 'Colonoscopia total', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a702ce9d-1d85-4ef4-a745-17d9594a3f67'),
('SUR-012', 'Lavado peritoneal diagnÃ³stico', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'a5dc1b20-e569-44fd-b51b-6b9e2eb22429'),
('SUR-013', 'Biopsia de piel por punch', 'PROCEDURE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd3068d6d-62e3-4aec-8f7d-847566506ccb');

-- Se pueden aÃ±adir cientos mÃ¡s segÃºn norma tÃ©cnica nacional.

