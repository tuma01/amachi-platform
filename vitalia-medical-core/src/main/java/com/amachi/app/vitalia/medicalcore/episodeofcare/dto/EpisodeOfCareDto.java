package com.amachi.app.vitalia.medicalcore.episodeofcare.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.EpisodeOfCareStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "EpisodeOfCare", description = "Agrupador de eventos clínicos de cuidado continuo (FHIR EpisodeOfCare)")
public class EpisodeOfCareDto {

    @Schema(description = "Identificador único", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @NotNull(message = "Médico responsable {err.mandatory}")
    @Schema(description = "ID del médico responsable del episodio", example = "2001")
    private Long managingDoctorId;

    @Schema(description = "Nombre completo del médico (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String managingDoctorFullName;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado del episodio de cuidado", example = "ACTIVE")
    private EpisodeOfCareStatus status;

    @Schema(description = "Descripción del tipo de episodio", example = "EMBARAZO DE ALTO RIESGO")
    private String typeDescription;

    @NotNull(message = "Inicio del período {err.mandatory}")
    @Schema(description = "Fecha y hora de inicio del episodio", example = "2026-01-15T08:00:00Z")
    private OffsetDateTime periodStart;

    @Schema(description = "Fecha y hora de cierre del episodio (null si está activo)", example = "2026-09-30T10:00:00Z")
    private OffsetDateTime periodEnd;

    @Schema(description = "Objetivos clínicos del episodio de cuidado")
    private String goals;

    @Schema(description = "Notas adicionales del episodio")
    private String notes;
}
