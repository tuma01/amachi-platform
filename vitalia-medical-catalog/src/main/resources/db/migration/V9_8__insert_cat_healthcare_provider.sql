-- ============================================================
-- Script: V2_50_08__insert_cat_healthcare_provider.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_HEALTHCARE_PROVIDER (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_HEALTHCARE_PROVIDER (CODE, NAME, TAX_ID, OFFICIAL_EMAIL, OFFICIAL_PHONE, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('EPS-001', 'Sanitas EPS', '800.123.456-1', 'contact@sanitas.com', '018000919100', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9f98b65c-5880-48d5-9048-59797d1b7758'),
('EPS-002', 'Sura EPS', '800.987.654-2', 'servicio@sura.com.co', '018000519519', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '691aeb4b-a28f-4db6-8dc1-d1fa6adf615d'),
('EPS-003', 'Salud Total EPS', '800.555.444-3', 'info@saludtotal.com', '018000114563', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c847d3ab-c2bb-42b3-a001-bff81f49e19b'),
('EPS-004', 'Nueva EPS', '900.000.111-4', 'atencion@nuevaeps.com', '018000954400', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'bdf60582-ad52-47fa-b842-2300a2da49d1'),
('EPS-005', 'Compensar EPS', '860.066.942-5', 'eps@compensar.com', '3077025', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '0b2f7038-f430-4c02-abde-e56ec505cd26'),
('EPS-006', 'Coomeva EPS', '800.111.222-6', 'servicios@coomeva.com', '018000950123', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'da3590c5-27a4-428d-8097-4283ad3e59b3'),
('EPS-007', 'Famisanar EPS', '830.003.564-7', 'afiliacion@famisanar.com', '3078069', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'bea73d55-cf91-4b2d-87ec-d9b0172a0320'),
('EPS-008', 'Aliansalud EPS', '800.251.444-8', 'aliansalud@colmedica.com', '018000123703', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9f104024-906a-4c99-a9e3-4506ee2861ee'),
('EPS-009', 'Mutual Ser', '806.008.394-9', 'atencion@mutualser.org', '018000114444', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '7b0761ec-66fd-4ad7-be43-d9e706d22a74'),
('EPS-010', 'Particular / Sin Aseguradora', '000.000.000-0', 'particular@vitalia.com', '0', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '019a22dc-debe-4d5b-8023-819c9a4fe273'),
('EPS-011', 'Cajasan', '890.201.235-0', 'contacto@cajasan.com', '018000123456', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '7be14fd9-9b49-4f12-9869-b2d013c1d6e8'),
('EPS-012', 'Comfenalco', '890.900.842-6', 'servicios@comfenalco.com', '018000912233', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'cd405ef2-596d-48a2-9c75-68105ee0ece2'),
('EPS-013', 'Cafesalud (Liquidada)', '800.140.881-1', 'archivo@cafesalud.com', '0', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '4372e48e-db52-46c0-8620-cf8f0408fb40'),
('EPS-014', 'Saludvida (Liquidada)', '830.074.839-2', 'info@saludvida.com', '0', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'daa780c7-0627-40c9-981f-6da8b5cd616b'),
('EPS-015', 'Medimás (Liquidada)', '901.097.473-5', 'notificaciones@medimas.com.co', '0', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c4428ee8-57e1-41f1-a133-6e8190cdf54d');


