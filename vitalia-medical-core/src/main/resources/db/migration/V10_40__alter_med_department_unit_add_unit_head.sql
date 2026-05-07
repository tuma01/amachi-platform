-- ============================================================
-- V10_40__alter_med_department_unit_add_unit_head.sql
-- ALTER: MED_DEPARTMENT_UNIT
-- Activa la relación diferida con MED_EMPLOYEE como jefe de unidad (Fase 6).
-- ============================================================

ALTER TABLE MED_DEPARTMENT_UNIT
    ADD COLUMN FK_ID_UNIT_HEAD BIGINT NULL
        COMMENT 'Empleado designado como jefe de la unidad hospitalaria';

ALTER TABLE MED_DEPARTMENT_UNIT
    ADD CONSTRAINT FK_MED_DEPT_UNIT_HEAD
        FOREIGN KEY (FK_ID_UNIT_HEAD) REFERENCES MED_EMPLOYEE(ID);

ALTER TABLE MED_DEPARTMENT_UNIT
    ADD INDEX IDX_DEPT_UNIT_HEAD (FK_ID_UNIT_HEAD);
