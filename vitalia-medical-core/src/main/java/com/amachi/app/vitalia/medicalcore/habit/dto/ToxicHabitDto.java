package com.amachi.app.vitalia.medicalcore.habit.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.AlcoholConsumption;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "ToxicHabit", description = "Hábitos tóxicos y conductas de riesgo del paciente")
public class ToxicHabitDto {

    @Schema(description = "ID único", example = "801", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "Indica si es el registro vigente", example = "true")
    private Boolean isCurrent;

    @NotNull(message = "Consumo de alcohol {err.mandatory}")
    @Schema(description = "Nivel de consumo de alcohol", example = "OCCASIONAL")
    private AlcoholConsumption alcohol;

    @Schema(description = "Descripción del consumo de tabaco", example = "Fumador ocasional")
    private String tobacco;

    @Schema(description = "Cigarrillos por día", example = "5")
    private Integer cigarettesPerDay;

    @Schema(description = "Edad de inicio del hábito", example = "18")
    private Integer startAge;

    @Schema(description = "Fecha de abandono del hábito", example = "2020-01-01")
    private LocalDate quitDate;

    @Schema(description = "Uso de drogas", example = "Ninguno")
    private String drugs;

    @Schema(description = "Consumo de cafeína", example = "2 tazas de café al día")
    private String caffeine;

    @Schema(description = "Sedentarismo", example = "Trabajo de escritorio 8h/día")
    private String sedentaryLifestyle;

    @Schema(description = "Otros hábitos de riesgo")
    private String others;
}
