package com.amachi.app.vitalia.medicalcore.encounter.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * ≡ƒôô Solicitud de inicio de encuentro cl├¡nico derivado de una cita previa.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Schema(name = "StartEncounterRequest", description = "Solicitud para iniciar formalmente el acto médico desde una cita.")
public class StartEncounterRequest {

    @NotNull(message = "El ID de la cita es obligatorio")
    @Schema(description = "ID de la cita médica (Appointment) vinculada", example = "105")
    private Long appointmentId;

    @Schema(description = "Nota de inicio o motivo de apertura del encuentro", example = "Paciente presente en consultorio para chequeo anual.")
    private String startNote;
}
