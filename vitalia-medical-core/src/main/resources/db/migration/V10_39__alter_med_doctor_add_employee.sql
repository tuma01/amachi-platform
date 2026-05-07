-- ============================================================
-- V10_39__alter_med_doctor_add_employee.sql
-- ALTER: MED_DOCTOR
-- Activa la relación diferida con MED_EMPLOYEE (Fase 6).
-- ============================================================

ALTER TABLE MED_DOCTOR
    ADD COLUMN FK_ID_EMPLOYEE BIGINT NULL
        COMMENT 'Ficha de relación laboral del facultativo en RRHH';

ALTER TABLE MED_DOCTOR
    ADD CONSTRAINT FK_MED_DR_EMP
        FOREIGN KEY (FK_ID_EMPLOYEE) REFERENCES MED_EMPLOYEE(ID);

ALTER TABLE MED_DOCTOR
    ADD INDEX IDX_DOCTOR_EMPLOYEE (FK_ID_EMPLOYEE);
