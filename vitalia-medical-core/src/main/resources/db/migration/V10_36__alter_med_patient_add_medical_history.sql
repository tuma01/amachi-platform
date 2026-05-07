-- ============================================================
-- V10_36__alter_med_patient_add_medical_history.sql
-- ALTER: MED_PATIENT
-- Activa la relación diferida con MED_MEDICAL_HISTORY (Fase 6).
-- ============================================================

ALTER TABLE MED_PATIENT
    ADD COLUMN FK_ID_MEDICAL_HISTORY BIGINT NULL
        COMMENT 'Expediente clínico vigente del paciente (relación de conveniencia)';

ALTER TABLE MED_PATIENT
    ADD CONSTRAINT FK_MED_PAT_HIST
        FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID);

ALTER TABLE MED_PATIENT
    ADD INDEX IDX_PAT_MEDICAL_HIST (FK_ID_MEDICAL_HISTORY);
