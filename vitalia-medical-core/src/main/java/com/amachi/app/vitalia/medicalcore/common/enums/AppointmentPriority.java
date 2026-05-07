package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Nivel de prioridad clínica de una cita médica.
 * Determina el orden de atención en la agenda y recursos asignados.
 */
@Schema(description = "Nivel de prioridad de la cita médica")
public enum AppointmentPriority {
    NORMAL,     // Cita rutinaria sin urgencia
    URGENT,     // Requiere atención preferente
    VIP,        // Paciente con protocolo especial de atención
    CRITICAL    // Riesgo vital: atención inmediata requerida
}
