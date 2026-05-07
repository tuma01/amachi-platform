package com.amachi.app.vitalia.medicalcore.medicalhistory.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.MedicalHistoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "MedicalHistory", description = "Expediente clínico longitudinal del paciente (EHR)")
public class MedicalHistoryDto {

    @Schema(description = "ID único", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "ID del médico responsable del expediente", example = "2001")
    private Long responsibleDoctorId;

    @Schema(description = "Nombre del médico responsable (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String responsibleDoctorFullName;

    @NotBlank(message = "Número de historia {err.mandatory}")
    @Schema(description = "Número de historia clínica único por tenant", example = "HCL-2026-00001")
    private String historyNumber;

    @Schema(description = "UUID del documento electrónico", example = "a3f8c1d2-...")
    private String documentUuid;

    @NotNull(message = "Fecha de registro {err.mandatory}")
    @Schema(description = "Fecha de apertura del expediente", example = "2026-01-01")
    private LocalDate recordDate;

    @Schema(description = "Fecha de validez del expediente", example = "2030-12-31")
    private LocalDate validUntil;

    @Schema(description = "Indica si es el expediente vigente del paciente", example = "true")
    private Boolean isCurrent;

    @Schema(description = "Estado del expediente", example = "ACTIVE")
    private MedicalHistoryStatus status;

    @Schema(description = "Nivel de confidencialidad", example = "NORMAL")
    private String confidentialityLevel;

    @Schema(description = "Indica si el paciente es donante de órganos", example = "false")
    private Boolean isOrganDonor;

    @Schema(description = "Indica si el expediente está bloqueado para edición", example = "false")
    private Boolean isLocked;

    @Schema(description = "Observaciones generales del expediente")
    private String observations;

    @Schema(description = "Notas internas del expediente")
    private String notes;
}
