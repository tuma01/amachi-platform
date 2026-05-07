package com.amachi.app.vitalia.medicalcore.familyhistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "FamilyHistory", description = "Antecedentes familiares patológicos del paciente")
public class FamilyHistoryDto {

    @Schema(description = "ID único", example = "401", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "Indica si es el registro familiar vigente", example = "true")
    private Boolean isCurrent;

    @Schema(description = "Estado de salud de la madre")
    private String motherHealthInfo;

    @Schema(description = "Estado de salud del padre")
    private String fatherHealthInfo;

    @Schema(description = "Antecedentes cardíacos familiares")
    private String cardiacFamilyHistory;

    @Schema(description = "Antecedentes de salud mental familiares")
    private String mentalFamilyHistory;

    @Schema(description = "Antecedentes de diabetes familiares")
    private String diabetesFamilyHistory;

    @Schema(description = "Etnicidad familiar", example = "ANDINO")
    private String familyEthnicity;

    @Schema(description = "Índice de riesgo genético", example = "MEDIUM")
    private String geneticRiskIndex;

    @Schema(description = "Notas adicionales")
    private String additionalNotes;
}
