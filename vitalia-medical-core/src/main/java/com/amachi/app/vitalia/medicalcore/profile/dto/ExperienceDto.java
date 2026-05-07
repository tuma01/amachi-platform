package com.amachi.app.vitalia.medicalcore.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Experience", description = "Trayectoria laboral previa del profesional")
public class ExperienceDto {

    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Cargo o título {err.mandatory}")
    @Schema(description = "Cargo o posición desempeñada", example = "Jefe de Enfermería UCI")
    private String title;

    @NotBlank(message = "Institución {err.mandatory}")
    @Schema(description = "Centro de trabajo u organización", example = "Hospital Público San Juan de Dios")
    private String institution;

    @NotNull(message = "Fecha de inicio {err.mandatory}")
    @Schema(description = "Inicio del período laboral", example = "2016-01-01")
    private LocalDate startDate;

    @Schema(description = "Fin del período laboral (null si actual)", example = "2020-06-30")
    private LocalDate endDate;

    @Schema(description = "Descripción de responsabilidades y logros", example = "Coordinación de turnos y protocolos de UCI neonatal")
    private String description;
}
