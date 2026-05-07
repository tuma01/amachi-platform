package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de admisión hospitalaria")
public enum AdmissionType {
    @Schema(description = "Ingreso por área de urgencias")
    EMERGENCY,
    @Schema(description = "Ingreso programado por cirugía o tratamiento")
    PLANNED,
    @Schema(description = "Ingreso para observación corta")
    OBSERVATION,
    @Schema(description = "Ingreso por consulta externa")
    OUTPATIENT
}
