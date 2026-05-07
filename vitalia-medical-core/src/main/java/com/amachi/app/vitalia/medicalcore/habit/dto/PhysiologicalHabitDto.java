package com.amachi.app.vitalia.medicalcore.habit.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.SleepQuality;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "PhysiologicalHabit", description = "Hábitos fisiológicos del paciente")
public class PhysiologicalHabitDto {

    @Schema(description = "ID único", example = "701", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "Indica si es el registro vigente", example = "true")
    private Boolean isCurrent;

    @Schema(description = "Descripción nutricional", example = "Dieta balanceada con bajo consumo de sal")
    private String nutrition;

    @Schema(description = "Calidad del sueño", example = "GOOD")
    private SleepQuality sleepQuality;

    @Schema(description = "Horas de sueño diarias", example = "7")
    private Integer sleepHours;

    @Schema(description = "Patrón de micción", example = "Normal, 6 veces al día")
    private String urination;

    @Schema(description = "Patrón de defecación", example = "Regular, diaria")
    private String defecation;

    @Schema(description = "Información sobre actividad sexual")
    private String sexuality;

    @Schema(description = "Actividad deportiva", example = "Caminata 30 min diaria")
    private String sportsActivity;

    @Schema(description = "Otros hábitos relevantes")
    private String others;
}
