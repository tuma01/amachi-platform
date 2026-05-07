package com.amachi.app.vitalia.medicalcore.profile.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Conference", description = "Participación en congreso, simposio o evento científico")
public class ConferenceDto {

    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Tema {err.mandatory}")
    @Schema(description = "Tema o ponencia presentada", example = "Enfermería basada en evidencia en UCI neonatal")
    private String topic;

    @Schema(description = "Descripción del evento o contenido")
    private String description;

    @Schema(description = "Organización responsable del evento", example = "FELANPE")
    private String organizer;

    @Schema(description = "Ciudad y país del evento", example = "Lima, Perú")
    private String location;

    @Schema(description = "Indica si fue un evento de carácter internacional", example = "true")
    private Boolean isInternational;

    @NotNull(message = "Fecha del evento {err.mandatory}")
    @Schema(description = "Fecha de realización del congreso", example = "2023-11-10")
    private LocalDate date;
}
