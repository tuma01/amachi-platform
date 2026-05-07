package com.amachi.app.vitalia.medicalcore.employee.dto;

import com.amachi.app.core.common.enums.EmployeeStatus;
import com.amachi.app.core.common.enums.EmployeeType;
import com.amachi.app.core.common.enums.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Employee", description = "Registro laboral del personal hospitalario administrativo y de soporte")
public class EmployeeDto {

    @Schema(description = "Identificador único del empleado", example = "501", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Persona {err.mandatory}")
    @Schema(description = "ID de la identidad global (Person)", example = "10001")
    private Long personId;

    @Schema(description = "Nombre completo del empleado (solo lectura)", example = "CARLA QUISPE MAMANI", accessMode = Schema.AccessMode.READ_ONLY)
    private String personFullName;

    @Schema(description = "ID del usuario del sistema vinculado", example = "2001")
    private Long userId;

    @Schema(description = "ID de la unidad hospitalaria de adscripción", example = "101")
    private Long departmentUnitId;

    @Schema(description = "Nombre de la unidad (solo lectura)", example = "ADMINISTRACIÓN CENTRAL", accessMode = Schema.AccessMode.READ_ONLY)
    private String departmentUnitName;

    @Schema(description = "Código único interno del empleado", example = "EMP-2024-001")
    private String employeeCode;

    @NotNull(message = "Tipo de empleado {err.mandatory}")
    @Schema(description = "Clasificación del tipo de personal")
    private EmployeeType employeeType;

    @Schema(description = "Estado laboral actual")
    private EmployeeStatus employeeStatus;

    @Schema(description = "Contexto de rol profesional", example = "ADMINISTRATIVE")
    private String professionalRole;

    @Schema(description = "Cargo o puesto de trabajo", example = "Jefe de Archivo Médico")
    private String jobPosition;

    @Schema(description = "Fecha de ingreso formal a la institución", example = "2021-03-15")
    private LocalDate hireDate;

    @Schema(description = "Salario base mensual", example = "3500.00")
    private BigDecimal salary;

    @Schema(description = "Modalidad de contratación", example = "FULL_TIME")
    private String employmentType;

    @Schema(description = "Turno de trabajo asignado")
    private ShiftEnum workShift;

    @Schema(description = "Contacto de emergencia del empleado", example = "Pedro Quispe — 70012345")
    private String emergencyContact;
}
