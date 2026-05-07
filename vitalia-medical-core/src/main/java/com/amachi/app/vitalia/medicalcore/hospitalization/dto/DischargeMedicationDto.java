package com.amachi.app.vitalia.medicalcore.hospitalization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "DischargeMedication", description = "Medicación prescrita en el alta hospitalaria")
public class DischargeMedicationDto {

    @Schema(description = "Identificador único (PK)", example = "9001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Hospitalización {err.mandatory}")
    @Schema(description = "ID de la hospitalización padre", example = "8001")
    private Long hospitalizationId;

    @Schema(description = "ID del medicamento del catálogo", example = "200")
    private Long medicationId;

    @Schema(description = "Nombre del medicamento del catálogo (Solo lectura)", example = "AMOXICILINA 500MG", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicationCatalogName;

    @Schema(description = "Nombre o presentación específica prescrita", example = "AMOXICILINA 500MG CÁPSULAS")
    private String medicationName;

    @NotNull(message = "Dosis {err.mandatory}")
    @Schema(description = "Dosis del medicamento", example = "500mg")
    private String dosage;

    @NotNull(message = "Frecuencia {err.mandatory}")
    @Schema(description = "Frecuencia de administración", example = "Cada 8 horas")
    private String frequency;

    @Schema(description = "Duración del tratamiento", example = "7 días")
    private String duration;

    @Schema(description = "Instrucciones adicionales de administración")
    private String instructions;
}
