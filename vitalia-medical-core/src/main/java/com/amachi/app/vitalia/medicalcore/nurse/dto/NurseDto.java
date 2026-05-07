package com.amachi.app.vitalia.medicalcore.nurse.dto;

import com.amachi.app.core.common.enums.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Nurse", description = "Perfil del personal de enfermería y cuidados asistenciales")
public class NurseDto {

    @Schema(description = "Identificador único", example = "601", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Persona {err.mandatory}")
    @Schema(description = "ID de la identidad global (Person)", example = "10005")
    private Long personId;

    @Schema(description = "Nombre completo (solo lectura)", example = "ROSA MAMANI TICONA", accessMode = Schema.AccessMode.READ_ONLY)
    private String personFullName;

    @Schema(description = "ID del usuario del sistema vinculado", example = "2005")
    private Long userId;

    @Schema(description = "ID de la unidad hospitalaria de adscripción", example = "102")
    private Long departmentUnitId;

    @Schema(description = "Nombre de la unidad (solo lectura)", example = "UCI NEONATAL — ALA ESTE", accessMode = Schema.AccessMode.READ_ONLY)
    private String departmentUnitName;

    @Schema(description = "ID del perfil curricular vinculado", example = "301")
    private Long userProfileId;

    @Schema(description = "Número de licencia profesional de enfermería", example = "ENF-BOL-00123")
    private String licenseNumber;

    @Schema(description = "Rango o categoría dentro de enfermería", example = "LICENCIADA")
    private String rank;

    @Schema(description = "Turno de trabajo asignado")
    private ShiftEnum workShift;

    @Schema(description = "Fecha de vencimiento de la licencia", example = "2030-12-31")
    private LocalDate licenseExpiryDate;

    @Schema(description = "Fecha de ingreso formal a la institución", example = "2019-05-01")
    private LocalDate hireDate;

    @Schema(description = "Modalidad de contratación", example = "FULL_TIME")
    private String contractType;

    @Schema(description = "Contacto de emergencia", example = "Juan Ticona — 60098765")
    private String emergencyContact;

    @Schema(description = "Habilidades clínicas especializadas (UCI, Neonatología, etc.)")
    @Builder.Default
    private Set<String> clinicalSkills = new HashSet<>();
}
