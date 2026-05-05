package com.amachi.app.vitalia.medicalcore.encounter.dto;

import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.ConditionType;
import com.amachi.app.vitalia.medicalcore.common.enums.Severity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

/**
 * Schema for recording pathologies and clinical diagnoses (FHIR Condition).
 * Used for the longitudinal tracking of patient health records.
 */
@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Condition", description = "Unified clinical diagnosis schema (FHIR Condition / ICD-10 Compliant)")
public class ConditionDto {

    @Schema(description = "Internal unique identifier (PK)", example = "15001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Global Condition Identifier (FHIR/HID)", example = "COND-456-UUID", accessMode = Schema.AccessMode.READ_ONLY)
    private String externalId;

    @NotNull(message = "Patient {err.mandatory}")
    @Schema(description = "Identifier of the diagnosed patient", example = "5001")
    private Long patientId;

    @Schema(description = "Full name of the patient (Calculated)", example = "JOHN DOE", accessMode = Schema.AccessMode.READ_ONLY)
    private String patientFullName;

    @Schema(description = "Identifier of the associated clinical encounter", example = "12001")
    private Long encounterId;

    @Schema(description = "Identifier of the assigned ICD-10 code (Catalog Ref)", example = "1")
    private Long icd10Id;

    @Schema(description = "ICD-10 Code string (Calculated)", example = "A00.0", accessMode = Schema.AccessMode.READ_ONLY)
    private String icd10Code;

    @Schema(description = "Name of the diagnosis (Literal/Display)", example = "Cholera due to Vibrio cholerae 01, biotype cholerae")
    private String name;

    @NotNull(message = "Doctor {err.mandatory}")
    @Schema(description = "Identifier of the practitioner who recorded the diagnosis", example = "2001")
    private Long doctorId;

    @Schema(description = "Full name of the practitioner (Calculated)", example = "DR. MARCOS SOLIZ", accessMode = Schema.AccessMode.READ_ONLY)
    private String doctorFullName;

    @NotNull(message = "Clinical Status {err.mandatory}")
    @Schema(description = "Current clinical status of the condition (ACTIVE, RECURRENCE, RELAPSE, INACTIVE, REMISSION, RESOLVED)", example = "ACTIVE")
    private ClinicalStatus clinicalStatus;

    @NotNull(message = "Condition Type {err.mandatory}")
    @Schema(description = "Category of the diagnosis (PROBLEM_LIST_ITEM, ENCOUNTER_DIAGNOSIS)", example = "ENCOUNTER_DIAGNOSIS")
    private ConditionType conditionType;

    @Schema(description = "Severity level of the pathology (SEVERE, MODERATE, MILD)", example = "MODERATE")
    private Severity severity;

    @Schema(description = "Associated clinical symptoms", example = "Severe diarrhea, dehydration")
    private String symptoms;

    @Schema(description = "Specific treatment notes for this condition", example = "Immediate intravenous hydration")
    private String treatmentNotes;

    @NotNull(message = "Diagnosis Date {err.mandatory}")
    @Schema(description = "Formal date when the diagnosis was established", example = "2026-03-30")
    private LocalDate diagnosisDate;

    @Schema(description = "Date when the condition was resolved or abated", example = "2026-04-10")
    private LocalDate abatementDate;

    @Schema(description = "Identifier of the parent Episode of Care (if applicable)", example = "301")
    private Long episodeOfCareId;

    @Schema(description = "Identifier of the linked master clinical record (reserved for future use)", example = "102")
    private Long medicalHistoryId;
}
