-- ============================================================
-- V10_14__alter_med_doctor_add_infrastructure.sql
-- ALTER: MED_DOCTOR
-- Agrega FK a DepartmentUnit y campo de disponibilidad operativa.
-- Activado al descomentar campos en Doctor.java (Phase 1).
-- ============================================================

-- Columna de unidad hospitalaria de adscripción del médico
ALTER TABLE MED_DOCTOR
    ADD COLUMN FK_ID_DEPT_UNIT  BIGINT      NULL
        COMMENT 'Unidad hospitalaria de adscripción del facultativo',
    ADD COLUMN AVAILABILITY_STATUS VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE'
        COMMENT 'Estado de disponibilidad operativa: AVAILABLE, IN_SURGERY, BUSY, ON_LEAVE, OFFLINE';

-- FK a la unidad hospitalaria
ALTER TABLE MED_DOCTOR
    ADD CONSTRAINT FK_MED_DR_DEPT
        FOREIGN KEY (FK_ID_DEPT_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID);

-- Índice para búsquedas por unidad
ALTER TABLE MED_DOCTOR
    ADD INDEX IDX_DOCTOR_DEPT_UNIT (FK_ID_DEPT_UNIT);
