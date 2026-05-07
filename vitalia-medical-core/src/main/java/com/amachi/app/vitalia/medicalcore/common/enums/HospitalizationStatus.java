package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ciclo de vida integral de la hospitalización (FHIR Encounter.class = inpatient).
 * Controla cada fase desde la planificación hasta el egreso del paciente internado.
 */
@Schema(description = "Estado del ciclo de vida de la hospitalización")
public enum HospitalizationStatus {
    PLANNED,      // Ingreso programado — cama reservada, paciente no llegó
    ADMITTED,     // Paciente formalmente ingresado al centro
    ACTIVE,       // Estancia activa en unidad de hospitalización
    TRANSFERRED,  // Trasladado a otra unidad, sala o centro externo
    DISCHARGED,   // Paciente con alta médica firmada
    ABSCONDED,    // Se retiró sin autorización médica (fuga)
    DECEASED,     // Fallecimiento durante la estancia hospitalaria
    CANCELLED     // Registro de ingreso anulado antes de materializarse
}
