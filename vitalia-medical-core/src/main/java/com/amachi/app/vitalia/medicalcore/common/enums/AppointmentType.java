package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Clasificación clínica del acto programado en la agenda médica.
 * Define el propósito y recursos necesarios para la cita.
 */
@Schema(description = "Tipo clínico de la cita médica")
public enum AppointmentType {
    CONSULTATION,   // Primera consulta o consulta general
    FOLLOW_UP,      // Control o seguimiento de tratamiento activo
    EMERGENCY,      // Atención urgente no programada
    CONTROL,        // Revisión periódica de condición crónica
    REVIEW,         // Evaluación de resultados (labs, imagen, etc.)
    PROCEDURE       // Intervención o procedimiento clínico menor
}
