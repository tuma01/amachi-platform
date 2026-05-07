package com.amachi.app.vitalia.medicalcore.professional.dto;

import com.amachi.app.core.common.enums.ProfessionalRoleContext;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "ProfessionalInfo", description = "Registro de trayectoria laboral del profesional de salud por período")
public class ProfessionalInfoDto {

    @Schema(description = "Identificador único", example = "701", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Persona {err.mandatory}")
    @Schema(description = "ID de la identidad global (Person)", example = "10005")
    private Long personId;

    @Schema(description = "ID de la organización donde trabajó", example = "200")
    private Long organizationId;

    @NotNull(message = "Fecha de inicio {err.mandatory}")
    @Schema(description = "Inicio del período laboral", example = "2015-01-01")
    private LocalDate periodStartDate;

    @Schema(description = "Fin del período laboral (null si posición actual)", example = "2020-12-31")
    private LocalDate periodEndDate;

    @Schema(description = "Indica si es la posición laboral actual", example = "true")
    private Boolean isCurrent;

    @NotNull(message = "Contexto de rol {err.mandatory}")
    @Schema(description = "Contexto funcional del rol profesional")
    private ProfessionalRoleContext roleContext;

    @Schema(description = "Cargo o posición dentro de la organización", example = "Supervisora de Enfermería")
    private String position;

    @Schema(description = "Área o departamento de adscripción", example = "Enfermería UCI")
    private String department;

    @Schema(description = "Código de empleado en la organización externa", example = "HOSP-2015-0042")
    private String employeeId;

    @Schema(description = "Número de licencia profesional vigente en el período", example = "ENF-BOL-00123")
    private String licenseNumber;

    @Schema(description = "Años de experiencia al inicio del período", example = "5")
    private Integer yearsOfExperienceAtStart;

    @Schema(description = "Modalidad de contratación", example = "FULL_TIME")
    private String contractType;

    @Schema(description = "Horario de trabajo", example = "Lunes a Viernes 08:00-16:00")
    private String workSchedule;

    @Schema(description = "Grado salarial o banda salarial", example = "GS-7")
    private String salaryGrade;

    @Schema(description = "Nombre del supervisor directo", example = "Dra. Carmen Flores")
    private String supervisor;

    @Schema(description = "Calificación de desempeño (1.0 - 5.0)", example = "4.5")
    private BigDecimal performanceRating;

    @Schema(description = "Notas adicionales del período")
    private String notes;
}
