-- ============================================================
-- V10_15__alter_med_appointment_add_infrastructure.sql
-- ALTER: MED_APPOINTMENT
-- Activa FK a DepartmentUnit y Room (estaban comentadas en el DDL original).
-- APPOINTMENT_TYPE cambia de VARCHAR(50) a VARCHAR(30) para enum tipado.
-- PRIORITY cambia de VARCHAR(20) a VARCHAR(20) con enum AppointmentPriority.
-- ============================================================

-- Columnas de infraestructura hospitalaria
ALTER TABLE MED_APPOINTMENT
    ADD COLUMN FK_ID_UNIT   BIGINT NULL
        COMMENT 'Unidad/departamento hospitalario asignado a la cita',
    ADD COLUMN FK_ID_ROOM   BIGINT NULL
        COMMENT 'Consultorio o box físico asignado a la cita';

-- Ajuste de longitud del tipo de cita para alinearlo con el enum AppointmentType
ALTER TABLE MED_APPOINTMENT
    MODIFY COLUMN APPOINTMENT_TYPE VARCHAR(30) NULL
        COMMENT 'Tipo: CONSULTATION, FOLLOW_UP, EMERGENCY, CONTROL, REVIEW, PROCEDURE';

-- FK a infraestructura
ALTER TABLE MED_APPOINTMENT
    ADD CONSTRAINT FK_MED_APP_UNIT
        FOREIGN KEY (FK_ID_UNIT) REFERENCES MED_DEPARTMENT_UNIT(ID),
    ADD CONSTRAINT FK_MED_APP_ROOM
        FOREIGN KEY (FK_ID_ROOM) REFERENCES MED_ROOM(ID);

-- Índices de infraestructura
ALTER TABLE MED_APPOINTMENT
    ADD INDEX IDX_APP_UNIT (FK_ID_UNIT),
    ADD INDEX IDX_APP_ROOM (FK_ID_ROOM);
