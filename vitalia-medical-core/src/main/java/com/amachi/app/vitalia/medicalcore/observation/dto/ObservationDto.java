package com.amachi.app.vitalia.medicalcore.observation.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.ObservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

/**
 * Schema for clinical measurements and diagnostic findings (FHIR Observation).
 * Provides standardized support for Vital Signs and Laboratory Results.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Observation", description = "Unified schema for clinical findings or measurements (Hospital Grade / FHIR Compliant)")
public class ObservationDto {

    @Schema(description = "Internal unique identifier (PK)", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Global Observation Identifier (FHIR/HID)", example = "OBS-789-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    @NotNull(message = "Patient {err.mandatory}")
    @Schema(description = "Identifier of the patient associated with the observation", example = "5001")
    private Long patientId;

    @Schema(description = "Full name of the patient (Calculated)", example = "JOHN DOE", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "Identifier of the associated clinical encounter", example = "12001")
    private Long encounterId;

    @NotNull(message = "Practitioner {err.mandatory}")
    @Schema(description = "Identifier of the practitioner recording the measurement", example = "2001")
    private Long doctorId;

    @Schema(description = "Full name of the practitioner (Calculated)", example = "DR. MARCOS SOLIZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @NotNull(message = "Status {err.mandatory}")
    @Schema(description = "Status of the measurement (REGISTERED, PRELIMINARY, FINAL, AMENDED, CANCELLED)", example = "FINAL")
    private ObservationStatus status;

    @NotNull(message = "Code {err.mandatory}")
    @Schema(description = "Clinical code (e.g., LOINC, Atomic Code)", example = "8867-4")
    private String code;

    @Schema(description = "Descriptive name of the test or finding", example = "Heart Rate")
    private String name;

    @NotNull(message = "Value {err.mandatory}")
    @Schema(description = "Recorded clinical value", example = "72")
    private String value;

    @Schema(description = "Unit of measurement", example = "bpm")
    private String unit;

    @Schema(description = "Normal reference range for interpretation", example = "60-100 bpm")
    private String referenceRange;

    @Schema(description = "Clinical interpretation (NORMAL, HIGH, LOW, CRITICAL)", example = "NORMAL")
    private String interpretation;

    @NotNull(message = "Effective DateTime {err.mandatory}")
    @Schema(description = "Timestamp when the measurement was performed", example = "2026-03-30T10:35:00Z")
    private OffsetDateTime effectiveDateTime;

    @Schema(description = "Additional clinical notes or comments", example = "Patient at rest")
    private String notes;
}
