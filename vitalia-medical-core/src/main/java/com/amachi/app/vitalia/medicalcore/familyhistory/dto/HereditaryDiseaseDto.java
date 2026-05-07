package com.amachi.app.vitalia.medicalcore.familyhistory.dto;

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
@Schema(name = "HereditaryDisease", description = "Enfermedad hereditaria dentro del historial familiar")
public class HereditaryDiseaseDto {

    @Schema(description = "ID único", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Historial familiar {err.mandatory}")
    @Schema(description = "ID del historial familiar", example = "401")
    private Long familyHistoryId;

    @NotBlank(message = "Nombre {err.mandatory}")
    @Schema(description = "Nombre de la patología hereditaria", example = "Hemophilia A")
    private String name;

    @Schema(description = "ID del parentesco afectado (catálogo)", example = "3")
    private Long kinshipId;

    @Schema(description = "Nombre del parentesco (solo lectura)", example = "Abuelo paterno", accessMode = Schema.AccessMode.READ_ONLY)
    private String kinshipName;

    @Schema(description = "Observaciones clínicas de la patología")
    private String remark;

    @Schema(description = "Fecha aproximada de diagnóstico familiar", example = "2000-03-15")
    private LocalDate diagnosisDate;
}
