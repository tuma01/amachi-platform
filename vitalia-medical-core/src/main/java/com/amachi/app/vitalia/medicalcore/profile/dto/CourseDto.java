package com.amachi.app.vitalia.medicalcore.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Course", description = "Curso, diplomado o capacitación continua del profesional")
public class CourseDto {

    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Título del curso {err.mandatory}")
    @Schema(description = "Nombre del curso o diplomado", example = "Cuidados Intensivos Neonatales")
    private String title;

    @NotBlank(message = "Institución {err.mandatory}")
    @Schema(description = "Institución que impartió el curso", example = "Sociedad Boliviana de Pediatría")
    private String institution;

    @Schema(description = "Descripción breve del contenido", example = "Manejo avanzado de ventiladores y monitores neonatales")
    private String description;

    @NotNull(message = "Fecha del curso {err.mandatory}")
    @Schema(description = "Fecha de realización", example = "2022-08-15")
    private LocalDate courseDate;

    @NotNull(message = "Duración en horas {err.mandatory}")
    @Min(value = 1, message = "La duración mínima es 1 hora")
    @Schema(description = "Duración total en horas", example = "40")
    private Integer durationInHours;

    @Schema(description = "Código o referencia del certificado emitido", example = "SBP-2022-UCI-0042")
    private String certificate;
}
