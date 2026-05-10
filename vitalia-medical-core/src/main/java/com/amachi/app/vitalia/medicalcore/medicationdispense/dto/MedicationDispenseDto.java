package com.amachi.app.vitalia.medicalcore.medicationdispense.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.DispenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "MedicationDispense", description = "Dispensación de medicamento (FHIR MedicationDispense)")
public class MedicationDispenseDto {

    @Schema(description = "ID único", example = "7001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Paciente {err.mandatory}")
    @Schema(description = "ID del paciente receptor", example = "5001")
    private Long patientId;

    @Schema(description = "Nombre del paciente (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @NotNull(message = "Prescripción {err.mandatory}")
    @Schema(description = "ID de la prescripción origen", example = "6001")
    private Long prescriptionId;

    @Schema(description = "ID del encuentro asociado", example = "12001")
    private Long encounterId;

    @Schema(description = "ID del dispensador (médico/farmacéutico)", example = "2001")
    private Long dispenserId;

    @Schema(description = "Nombre del dispensador (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String dispenserFullName;

    @Schema(description = "ID del medicamento del catálogo", example = "301")
    private Long medicationId;

    @Schema(description = "Nombre del medicamento (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String medicationName;

    @NotNull(message = "Estado {err.mandatory}")
    @Schema(description = "Estado de la dispensación", example = "COMPLETED")
    private DispenseStatus status;

    @Schema(description = "Cantidad dispensada", example = "30.00")
    private BigDecimal quantity;

    @Min(value = 0, message = "Días de suministro {err.min.value}")
    @Schema(description = "Días de suministro cubiertos", example = "30")
    private Integer daysSupply;

    @Size(max = 50, message = "Número de lote {err.max.length}")
    @Schema(description = "Número de lote del medicamento", example = "LOT-2026-001")
    private String lotNumber;

    @Schema(description = "Fecha y hora de dispensación", example = "2026-03-30T10:30:00Z")
    private OffsetDateTime dispensedAt;

    @Schema(description = "Fecha y hora de entrega física al paciente", example = "2026-03-30T10:45:00Z")
    private OffsetDateTime handedOverAt;

    @Size(max = 2000, message = "Nota del dispensador {err.max.length}")
    @Schema(description = "Notas del dispensador", example = "Tomar con alimentos")
    private String dispenserNote;
}
