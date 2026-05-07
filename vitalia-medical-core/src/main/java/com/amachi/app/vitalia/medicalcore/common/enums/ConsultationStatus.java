package com.amachi.app.vitalia.medicalcore.common.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados del flujo de consulta médica")
public enum ConsultationStatus {
    @Schema(description = "Cita programada con antelación")
    SCHEDULED,
    @Schema(description = "Paciente en sala de espera")
    WAITING,
    @Schema(description = "Atención médica activa")
    IN_PROGRESS,
    @Schema(description = "Atención finalizada")
    COMPLETED,
    @Schema(description = "Cita anulada")
    CANCELLED,
    @Schema(description = "Cita cambiada a otra fecha")
    RESCHEDULED,
    @Schema(description = "El paciente no se presentó")
    NO_SHOW
}
