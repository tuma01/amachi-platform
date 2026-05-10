package com.amachi.app.vitalia.medicalcore.medicationadministration.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.AdministrationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "MedicationAdministration", description = "Administración de medicamento al paciente (FHIR MedicationAdministration)")
public class MedicationAdministrationDto {

    @Schema(description = "ID único", example = "8001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre del paciente (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @NotNull(message = "Prescripción {err.mandatory}")
    @Schema(description = "ID de la prescripción origen", example = "6001")
    private Long prescriptionId;

    @Schema(description = "ID de la dispensación asociada", example = "7001")
    private Long dispenseId;

    @Schema(description = "ID del encuentro clínico", example = "12001")
    private Long encounterId;

    @Schema(description = "ID de la enfermera que administró", example = "3001")
    private Long nurseId;

    @Schema(description = "Nombre de la enfermera (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String nurseFullName;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado de la administración", example = "COMPLETED")
    private AdministrationStatus status;

    @Schema(description = "Fecha y hora de administración", example = "2026-03-30T08:00:00Z")
    private OffsetDateTime administeredAt;

    @Schema(description = "Dosis administrada", example = "1.00")
    private BigDecimal doseQuantity;

    @Size(max = 30, message = "Unidad de dosis {err.max.length}")
    @Schema(description = "Unidad de la dosis", example = "tab")
    private String doseUnit;

    @Size(max = 50, message = "Vía de administración {err.max.length}")
    @Schema(description = "Vía de administración", example = "oral")
    private String route;

    @Size(max = 2000, message = "Notas {err.max.length}")
    @Schema(description = "Notas de la administración", example = "Paciente toleró bien la dosis")
    private String notes;
}
