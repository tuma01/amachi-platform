package com.amachi.app.vitalia.medicalcore.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Insurance", description = "Seguro médico y cobertura del paciente")
public class InsuranceDto {

    @Schema(description = "ID único", example = "601", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Expediente {err.mandatory}")
    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @NotNull(message = "Proveedor de salud {err.mandatory}")
    @Schema(description = "ID del proveedor de salud (catálogo)", example = "10")
    private Long providerId;

    @Schema(description = "Nombre del proveedor (solo lectura)", accessMode = Schema.AccessMode.READ_ONLY)
    private String providerName;

    @Schema(description = "Número de póliza", example = "POL-2026-00123")
    private String policyNumber;

    @Schema(description = "Tipo de póliza", example = "PRIVADO")
    private String policyType;

    @Schema(description = "Fecha de inicio de cobertura", example = "2026-01-01")
    private LocalDate effectiveDate;

    @Schema(description = "Fecha de vencimiento de la póliza", example = "2026-12-31")
    private LocalDate expirationDate;

    @Schema(description = "Detalle de cobertura")
    private String coverageDetails;

    @Schema(description = "Monto de copago", example = "20.00")
    private BigDecimal copayAmount;

    @Schema(description = "Monto de deducible", example = "500.00")
    private BigDecimal deductibleAmount;

    @Schema(description = "Requiere autorización previa", example = "false")
    private Boolean requiresAuthorization;

    @Schema(description = "Indica si es el seguro vigente", example = "true")
    private Boolean isCurrent;
}
