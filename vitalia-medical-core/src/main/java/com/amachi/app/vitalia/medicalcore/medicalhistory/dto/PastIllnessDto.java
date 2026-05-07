package com.amachi.app.vitalia.medicalcore.medicalhistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "PastIllness", description = "Antecedente patológico personal del paciente")
public class PastIllnessDto {

    @Schema(description = "ID único", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @NotBlank(message = "Nombre {err.mandatory}")
    @Schema(description = "Nombre de la enfermedad pasada", example = "Appendicitis")
    private String name;

    @Schema(description = "Descripción de la enfermedad")
    private String description;

    @Schema(description = "Síntomas que presentó")
    private String symptoms;

    @Schema(description = "Tratamientos recibidos")
    private String treatments;
}
