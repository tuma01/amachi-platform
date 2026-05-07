package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Estado operativo de disponibilidad del médico en tiempo real.
 * Controla la agenda y asignación de citas y guardias.
 */
@Schema(description = "Estado de disponibilidad operativa del médico")
public enum DoctorAvailabilityStatus {
    AVAILABLE,    // Disponible para citas o asignaciones
    IN_SURGERY,   // En intervención quirúrgica activa
    BUSY,         // Atendiendo paciente o en actividad clínica
    ON_LEAVE,     // De vacaciones, licencia o permiso
    OFFLINE       // No disponible en el sistema (turno fuera, desconectado)
}
