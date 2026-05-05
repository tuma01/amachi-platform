package com.amachi.app.vitalia.medicalcore.observation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request to record a clinical observation or measurement (FHIR Observation).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to record a clinical observation (FHIR Observation)")
public class ObservationRequest {

    @NotNull(message = "Observation code is required")
    @Schema(description = "Standard clinical code (LOINC, SNOMED, etc.)", example = "8480-6")
    private String code;

    @NotNull(message = "Observation name is required")
    @Schema(description = "Human-readable description of the observation", example = "Blood Pressure")
    private String name;

    @NotNull(message = "Observation value is required")
    @Schema(description = "Measured value (numeric or textual)", example = "120/80")
    private String value;

    @Schema(description = "Measurement unit (UCUM standard)", example = "mmHg")
    private String unit;

    @Schema(description = "Clinical interpretation (HIGH, LOW, NORMAL)", example = "NORMAL")
    private String interpretation;

    @Schema(description = "Additional clinical notes")
    private String notes;
}
