-- ============================================================
-- V10_38__alter_med_condition_add_medical_history.sql
-- ALTER: MED_CONDITION
-- Activa la columna FK_ID_MEDICAL_HISTORY que estaba comentada en V10_22 (Fase 6).
-- ============================================================

ALTER TABLE MED_CONDITION
    ADD COLUMN FK_ID_MEDICAL_HISTORY BIGINT NULL
        COMMENT 'Expediente clínico al que pertenece el diagnóstico';

ALTER TABLE MED_CONDITION
    ADD CONSTRAINT FK_CONDITION_HIST
        FOREIGN KEY (FK_ID_MEDICAL_HISTORY) REFERENCES MED_MEDICAL_HISTORY(ID);

ALTER TABLE MED_CONDITION
    ADD INDEX IDX_COND_HIST (FK_ID_MEDICAL_HISTORY);
