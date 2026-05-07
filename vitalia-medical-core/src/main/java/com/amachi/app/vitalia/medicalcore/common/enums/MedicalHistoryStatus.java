package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Estado operativo del expediente médico longitudinal del paciente.
 * Controla el acceso, la editabilidad y el ciclo de vida del historial clínico.
 */
@Schema(description = "Estado del expediente médico del paciente")
public enum MedicalHistoryStatus {
    ACTIVE,     // Expediente activo y editable por el equipo clínico
    ARCHIVED,   // Expediente archivado: solo lectura (paciente inactivo)
    LOCKED,     // Bloqueado por proceso legal, auditoría o resolución médica
    PASSIVE     // Expediente pasivo por fallecimiento del paciente
}
