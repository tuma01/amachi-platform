-- ============================================================
-- Script: V2_50_10__insert_cat_medical_specialty.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_MEDICAL_SPECIALTY (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_MEDICAL_SPECIALTY (CODE, NAME, TARGET_PROFESSION, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES

-- Medicina
('ESP-001', 'Cardiología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2f3bfc3e-2827-4414-9c4a-f60b1ff97bf3'),
('ESP-002', 'Pediatría', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'df710e3e-b689-4155-b93f-b022e49acb95'),
('ESP-003', 'Ginecología y Obstetricia', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '35bc243d-1de7-49a1-96fe-94fceefb3d11'),
('ESP-004', 'Medicina Interna', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2ca20ade-7568-4e3d-b744-12cdca3d9a41'),
('ESP-005', 'Dermatología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'fb80611e-4459-4fb2-ab44-9d26f6c8b5f5'),
('ESP-006', 'Neurología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '62359926-a2d5-468d-b65b-d6ea1dd77f28'),
('ESP-007', 'Psiquiatría', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '092b3f3b-899a-49b4-89fb-cd0c035303bc'),
('ESP-008', 'Oftalmología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ba3a0b65-c93e-438f-a0c2-e42b18b45358'),
('ESP-009', 'Otorrinolaringología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8b01a71a-2fc9-4ed7-851f-0f477170ab61'),
('ESP-010', 'Ortopedia y Traumatología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd291a697-def6-4583-8b91-8d06b2ec24d9'),
('ESP-011', 'Anestesiología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'be1bd0fb-874e-4990-b84b-4f8a53c04a1b'),
('ESP-012', 'Urología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '77728145-4345-4f45-8f11-c111f00295c7'),
('ESP-013', 'Gastroenterología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '463c99e9-eea9-40bf-812c-d0adff4b5a68'),
('ESP-014', 'Endocrinología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'e6505b4d-8835-496f-a7e5-a54cdd9a8f27'),
('ESP-015', 'Nefrología', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '32f9dd90-d692-43f7-a04f-9f5e9b093660'),
('ESP-016', 'Cirugía General', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f3c2657a-57b5-42cb-92d7-98d29c425440'),
('ESP-017', 'Medicina Familiar', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3e58f3b7-31d8-4f61-9c61-a58f63f74aa6'),
('ESP-018', 'Medicina de Urgencias', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '1f0c030f-86ba-4598-b61b-4dcfc8276617'),
('ESP-019', 'RadiologÃ­a e ImÃ¡genes DiagnÃ³sticas', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '820cf670-4f06-42df-add9-71a0f0bf4370'),
('ESP-020', 'PatologÃ­a ClÃ­nica', 'DOCTOR', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'e20db594-f45d-4c4c-b341-f9c1661bdb64'),

-- Enfermería
('ENF-001', 'Enfermería en Cuidado CrÃ­tico', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '13c26fab-3bc3-4103-8232-8ec50250cf16'),
('ENF-002', 'Enfermería NefrolÃ³gica', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'f26eaeae-88d2-43af-9244-f2c22e9bb42b'),
('ENF-003', 'Enfermería Oncológica', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd7bf43f4-3fd2-4d2a-9540-472eb1977f12'),
('ENF-004', 'Enfermería PediÃ¡trica', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '6569ea18-5ddb-4dee-b198-592afbb4897d'),
('ENF-005', 'Enfermería de Salud Mental', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0742184d-a3cd-4775-817d-5d0475373d35'),
('ENF-006', 'Enfermería ObstÃ©trico-GinecolÃ³gica', 'NURSE', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4bd63333-1c8d-4fa9-8254-5001a8f34b7b'),
('ENF-007', 'Enfermería en Salud PÃºblica', 'BOTH', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b984c878-597d-4143-a8aa-eb6b081e53ae'),
('ENF-008', 'AdministraciÃ³n en Salud', 'BOTH', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8368d65e-14fd-4132-9fd3-fbac16eefb4a');
-- Lista extensible segÃºn normativas de cada paÃ­s.);


