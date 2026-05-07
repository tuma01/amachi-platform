package com.amachi.app.vitalia.medicalcore.medicalhistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "ActualIllness", description = "Enfermedad actual activa del paciente")
public class ActualIllnessDto {

    @Schema(description = "ID único", example = "201", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @NotBlank(message = "Nombre {err.mandatory}")
    @Schema(description = "Nombre de la enfermedad activa", example = "Hypertension")
    private String name;

    @Schema(description = "Fecha de diagnóstico", example = "2025-06-15")
    private LocalDate diagnosisDate;

    @Schema(description = "Sintomatología asociada")
    private String symptoms;

    @Schema(description = "Tratamientos previos o actuales")
    private String treatments;
}
