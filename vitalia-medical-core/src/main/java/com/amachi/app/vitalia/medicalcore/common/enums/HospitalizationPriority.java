package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Nivel de urgencia para el ingreso hospitalario.
 * Define la precedencia de asignación de cama y recursos clínicos.
 */
@Schema(description = "Prioridad de ingreso para hospitalización")
public enum HospitalizationPriority {
    SELECTIVE,    // Ingreso programado y diferible
    URGENT,       // Requiere ingreso en menos de 24 horas
    CRITICAL,     // Riesgo vital: ingreso inmediato en UCI o urgencias
    PALLIATIVE    // Cuidados paliativos: confort y calidad de vida
}
