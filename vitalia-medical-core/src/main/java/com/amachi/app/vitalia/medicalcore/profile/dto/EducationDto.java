package com.amachi.app.vitalia.medicalcore.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Education", description = "Formación académica del profesional de salud")
public class EducationDto {

    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Institución {err.mandatory}")
    @Schema(description = "Institución educativa", example = "Universidad Mayor de San Andrés")
    private String institution;

    @NotBlank(message = "Grado académico {err.mandatory}")
    @Schema(description = "Título o grado obtenido", example = "Licenciatura en Enfermería")
    private String degree;

    @NotNull(message = "Fecha de inicio {err.mandatory}")
    @Schema(description = "Fecha de inicio del programa", example = "2010-03-01")
    private LocalDate startDate;

    @Schema(description = "Fecha de graduación (null si en curso)", example = "2015-12-15")
    private LocalDate endDate;
}
