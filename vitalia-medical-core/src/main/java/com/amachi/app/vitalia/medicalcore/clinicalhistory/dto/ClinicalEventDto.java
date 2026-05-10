package com.amachi.app.vitalia.medicalcore.clinicalhistory.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Schema(description = "Evento clínico unificado para la línea de tiempo del paciente")
public class ClinicalEventDto {

    @Schema(description = "Tipo de evento clínico", example = "ENCOUNTER")
    private ClinicalEventType type;

    @Schema(description = "Fecha y hora del evento", example = "2026-03-30T10:30:00Z")
    private OffsetDateTime eventDate;

    @Schema(description = "Descripción legible del evento", example = "AMBULATORY — Dolor abdominal agudo")
    private String summary;

    @Schema(description = "ID del recurso origen", example = "12001")
    private Long referenceId;

    @Schema(description = "Código clínico asociado (CIE-10, LOINC)", example = "I10")
    private String code;

    @Schema(description = "Estado actual del evento", example = "COMPLETED")
    private String status;
}
