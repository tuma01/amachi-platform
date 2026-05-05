package com.amachi.app.vitalia.medicalcore.encounter.dto.request;

import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.ConditionType;
import com.amachi.app.vitalia.medicalcore.common.enums.Severity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Request to register a formal clinical diagnosis (FHIR Condition).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request to add a clinical condition (diagnosis) to an encounter")
public class ConditionRequest {

    @NotNull(message = "ICD-10 ID is required")
    @Schema(description = "ICD-10 catalog identifier", example = "45")
    private Long icd10Id;

    @NotNull(message = "Clinical status is required")
    @Schema(description = "Current clinical status (ACTIVE, INACTIVE, RESOLVED, etc.)")
    private ClinicalStatus clinicalStatus;

    @Schema(description = "Type of condition (ACUTE, CHRONIC, etc.)")
    private ConditionType conditionType;

    @Schema(description = "Severity level (MILD, MODERATE, SEVERE)")
    private Severity severity;

    @Schema(description = "Reported symptoms or justification", example = "Acute abdominal pain")
    private String symptoms;

    @Schema(description = "Additional treatment notes or observations")
    private String treatmentNotes;
}
