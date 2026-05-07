package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Modalidad de egreso del paciente al finalizar la hospitalización.
 * Alineado con estándares HL7 FHIR DischargeDisposition.
 */
@Schema(description = "Modalidad de egreso hospitalario del paciente")
public enum DischargeStatus {
    RECOVERED,       // Alta por mejoría clínica — resultado esperado
    VOLUNTARY,       // Alta voluntaria solicitada por el paciente
    REFERRED,        // Referido a otro centro / nivel de atención
    AWOL,            // Abandono sin autorización médica (Against Medical Advice)
    DECEASED,        // Fallecimiento durante la hospitalización
    ADMINISTRATIVE   // Egreso por causas administrativas o traslado interno
}
