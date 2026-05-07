package com.amachi.app.vitalia.medicalcore.prescription.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.MedicationRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Prescription", description = "Orden de medicación o receta electrónica (FHIR Prescription)")
public class PrescriptionDto {

    @Schema(description = "Identificador único", example = "8001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre completo del paciente (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "ID del encuentro clínico vinculado", example = "12001")
    private Long encounterId;

    @NotNull(message = "Médico {err.mandatory}")
    @Schema(description = "ID del médico prescriptor", example = "2001")
    private Long doctorId;

    @Schema(description = "Nombre completo del médico (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @Schema(description = "ID del medicamento del catálogo (opcional)", example = "301")
    private Long medicationId;

    @Schema(description = "Nombre del medicamento (texto libre si no está en catálogo)", example = "Paracetamol 500mg")
    private String medicationDisplayName;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado de la prescripción", example = "ACTIVE")
    private MedicationRequestStatus status;

    @Schema(description = "Fecha y hora de emisión de la receta", example = "2026-05-06T10:00:00Z")
    private OffsetDateTime authoredOn;

    @Schema(description = "Instrucciones de dosificación", example = "1 tableta cada 8 horas con alimentos")
    private String dosageInstruction;

    @Schema(description = "Vía de administración", example = "ORAL")
    private String route;

    @Schema(description = "Frecuencia de administración", example = "Q8H")
    private String frequency;

    @Schema(description = "Cantidad total prescrita", example = "30")
    private BigDecimal quantity;

    @Schema(description = "Prioridad de la prescripción", example = "ROUTINE")
    private String priority;

    @Schema(description = "Código de motivo de prescripción (CIE-10)", example = "J06.9")
    private String reasonCode;

    @Schema(description = "Notas adicionales del médico")
    private String notes;
}
