package com.amachi.app.vitalia.medicalcore.doctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "DoctorHospitalAssignment", description = "Vinculación del médico con centros hospitalarios")
public class DoctorHospitalAssignmentDto {

    @Schema(description = "Identificador único", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Médico {err.mandatory}")
    @Schema(description = "ID del médico", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre completo del médico (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @NotNull(message = "Hospital {err.mandatory}")
    @Schema(description = "ID del hospital", example = "1")
    private Long hospitalId;

    @Schema(description = "Nombre del hospital (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String hospitalName;

    @NotNull(message = "Fecha de inicio {err.mandatory}")
    @Schema(description = "Fecha de inicio de la asignación", example = "2026-01-01")
    private LocalDate startDate;

    @Schema(description = "Fecha de fin de la asignación (null si vigente)", example = "2026-12-31")
    private LocalDate endDate;

    @Schema(description = "Indica si es el hospital principal del médico", example = "true")
    private Boolean isPrimary;

    @Schema(description = "Rol del médico en ese hospital", example = "JEFE DE SERVICIO")
    private String roleInHospital;
}
