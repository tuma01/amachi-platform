package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Tipos de encuentro médico (SaaS Elite Tier - Clinical Standard).
 */
@Schema(description = "Categoría clínica del encuentro")
public enum EncounterType {
    @Schema(description = "Consulta externa / Ambulatoria")
    OUTPATIENT,
    
    @Schema(description = "Atención de urgencia")
    EMERGENCY,
    
    @Schema(description = "Internación / Hospitalización")
    INPATIENT,
    
    @Schema(description = "Consulta virtual o remota")
    TELEHEALTH,
    
    @Schema(description = "Visita domiciliaria")
    HOME_VISIT,
    
    @Schema(description = "Atención técnica o administrativa")
    ADMINISTRATIVE
}
