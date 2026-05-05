-- ============================================================
-- Script: V2_50_06__insert_cat_medication.sql
-- Módulo: vitalia-medical-catalog
-- Descripción: Inserción de la tabla CAT_MEDICATION (SaaS Elite - Catálogo Global).
-- ============================================================

INSERT INTO CAT_MEDICATION (CODE, GENERIC_NAME, COMMERCIAL_NAME, CONCENTRATION, PHARMACEUTICAL_FORM, PRESENTATION, IS_ACTIVE, CREATED_BY, CREATED_DATE, VERSION, EXTERNAL_ID) VALUES
('MED-001', 'Acetaminofén (Paracetamol)', 'Panadol', '500 mg', 'Tableta', 'Caja x 100 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '43a16dc5-dfe1-4169-9904-50e60412ce4a'),
('MED-002', 'Ibuprofeno', 'Advil', '400 mg', 'Tableta', 'Caja x 50 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'be0763f0-6ebd-49b8-9c39-19a9119cbaa8'),
('MED-003', 'Amoxicilina', 'Amoxil', '500 mg', 'Cápsula', 'Frasco x 100 Cápsulas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '218e6d60-43dc-4952-a02e-3acb2351d444'),
('MED-004', 'Metformina', 'Glucophage', '850 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '03095c3a-d5f3-4860-9160-a66880861959'),
('MED-005', 'Losartán Potásico', 'Cozaar', '50 mg', 'Tableta Recubierta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9277675a-18b5-47d3-a68c-301a3c636f12'),
('MED-006', 'Atorvastatina', 'Lipitor', '20 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '967936e7-94f4-4f27-95d5-9f18763a77d9'),
('MED-007', 'Omeprazol', 'Prilosec', '20 mg', 'Cápsula con gránulos entéricos', 'Frasco x 30 Cápsulas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3cf38173-8807-4f64-99c8-fc4bd62addfc'),
('MED-008', 'Salbutamol', 'Ventolin', '100 mcg/dosis', 'Inhalador de dosis medida', 'Frasco x 200 dosis', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd165253b-9993-4b29-b17e-8cb613af0060'),
('MED-009', 'Diclofenaco Sódico', 'Voltaren', '75 mg/3ml', 'SoluciÃ³n Inyectable', 'Ampolla x 3ml', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '91d71346-7cbd-4809-a398-fc5e486fa558'),
('MED-010', 'Dipirona', 'Novalgina', '1 g/2ml', 'SoluciÃ³n Inyectable', 'Ampolla x 2ml', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '1a63725e-8f62-4117-b4af-aeee7adfe2fe'),
('MED-011', 'Enalapril Maleato', 'Vasotec', '20 mg', 'Tableta', 'Caja x 50 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '540c1e43-09a8-4239-b3b4-f9baa0a8e2a9'),
('MED-012', 'Hidroclorotiazida', 'Microzide', '25 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '425618a1-30e1-4bcf-8d03-8c51cd873d0f'),
('MED-013', 'Ciprofloxacino', 'Cipro', '500 mg', 'Tableta Recubierta', 'Caja x 10 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'd42e3b35-128e-4c83-97e4-072ccf1bc5bd'),
('MED-014', 'Prednisolona', 'Delta-Cortef', '5 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b84d5bd4-754e-472e-ad97-d9c25f21e3eb'),
('MED-015', 'Loratadina', 'Claritin', '10 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'b3219a4c-84e1-4abd-91aa-e78fb3ea63c8'),
('MED-016', 'Insulina Humana NPH', 'Humulin N', '100 UI/ml', 'SuspensiÃ³n Inyectable', 'Vial x 10ml', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '1021144c-5b77-4b29-9d36-be2829e9d97d'),
('MED-017', 'Metoclopramida', 'Reglan', '10 mg', 'Tableta', 'Caja x 20 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '3e3484f5-d018-4813-91ac-24c0a98a4d53'),
('MED-018', 'Espironolactona', 'Aldactone', '25 mg', 'Tableta', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'c49afe97-cea6-445c-824e-3163a19ec87a'),
('MED-019', 'Tramadol Clorhidrato', 'Ultram', '50 mg', 'Cápsula', 'Caja x 20 Cápsulas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '38efbded-f6b6-443c-83f8-52c4583fdea0'),
('MED-020', 'Ãcido Acetilsalicílico', 'Aspirina', '100 mg', 'Tableta', 'Caja x 100 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8dda8c7c-d8b3-4c7d-b5c5-017c29992081'),
('MED-021', 'Ceftriaxona', 'Rocephin', '1 g', 'Polvo para inyectable', 'Frasco vial', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '36050b40-3fc1-4ee8-8452-180d043d6dfd'),
('MED-022', 'Levofloxacina', 'Levaquin', '500 mg', 'Tableta recubierta', 'Caja x 7 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ce61ef52-7738-4287-b373-ee8387cb840f'),
('MED-023', 'ValsartÃ¡n', 'Diovan', '160 mg', 'Tableta recubierta', 'Caja x 28 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '8217f9f4-8e70-4651-89de-da7025d49e9e'),
('MED-024', 'Metoprolol Sucinato', 'Toprol XL', '50 mg', 'Tableta de liberaciÃ³n prolongada', 'Caja x 30 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '6d018854-761c-4bbb-a610-98b5c945cd5c'),
('MED-025', 'Furosemida', 'Lasix', '40 mg', 'Tableta', 'Caja x 20 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '9cccd17d-50fe-43cc-94a9-90304ad11d72'),
('MED-026', 'Enoxaparina SÃ³dica', 'Lovenox', '40 mg/0.4ml', 'SoluciÃ³n en jeringa prellenada', 'Caja x 2 jeringas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '7d130c0b-25ca-482d-ab0d-80b782ee8322'),
('MED-027', 'Clopidogrel', 'Plavix', '75 mg', 'Tableta recubierta', 'Caja x 28 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'ef991e88-778c-43ad-870c-21744eb792ca'),
('MED-028', 'Dexametasona', 'Decadron', '4 mg/ml', 'SoluciÃ³n inyectable', 'Ampolla x 1ml', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '43d90abd-9207-4707-b5e8-cc6e3d98de4a'),
('MED-029', 'Fluconazol', 'Diflucan', '150 mg', 'Cápsula', 'Caja x 1 Cápsula', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, '2cb0a0cc-f3ae-4057-815f-9ea50d8efcf6'),
('MED-030', 'OndansetrÃ³n', 'Zofran', '8 mg', 'Tableta', 'Caja x 10 tabletas', 1, 'SYSTEM', CURRENT_TIMESTAMP, 0, 'dbbffa60-970b-47d1-9905-d7af05cfe519');


