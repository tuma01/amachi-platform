-- ============================================================
-- V10_37__alter_med_encounter_add_medical_history.sql
-- ALTER: MED_ENCOUNTER
-- Activa la relación diferida con MED_MEDICAL_HISTORY (Fase 6).
-- ============================================================

ALTER TABLE MED_ENCOUNTER
    ADD COLUMN FK_ID_HISTORY BIGINT NULL
        COMMENT 'Expediente clínico vinculado al encuentro';

ALTER TABLE MED_ENCOUNTER
    ADD CONSTRAINT FK_ENC_HISTORY
        FOREIGN KEY (FK_ID_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID);

ALTER TABLE MED_ENCOUNTER
    ADD INDEX IDX_ENC_MEDICAL_HIST (FK_ID_HISTORY);
