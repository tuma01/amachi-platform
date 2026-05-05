package com.amachi.app.vitalia.medicalcatalog.medication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Medication (Vademecum) Data Transfer Object (SaaS Elite Tier).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(name = "Medication", description = "Schema to hold Medication (Vademecum) information")
public class MedicationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Size(max = 20)
    @Schema(description = "Unique code of the medication", example = "MED-001")
    private String code;

    @JsonProperty("genericName")
    @NotBlank(message = "Generic Name {err.mandatory}")
    @Size(max = 250)
    @Schema(description = "Generic pharmacological name", example = "Paracetamol")
    private String genericName;

    @JsonProperty("commercialName")
    @Size(max = 250)
    @Schema(description = "Commercial brand name", example = "Tylenol")
    private String commercialName;

    @JsonProperty("concentration")
    @Size(max = 100)
    @Schema(description = "Concentration level (strength)", example = "500mg")
    private String concentration;

    @JsonProperty("pharmaceuticalForm")
    @Size(max = 100)
    @Schema(description = "Pharmaceutical dosage form", example = "TABLET")
    private String pharmaceuticalForm;

    @JsonProperty("presentation")
    @Size(max = 250)
    @Schema(description = "Package presentation", example = "BOX X 30 TABLETS")
    private String presentation;

    @JsonProperty("description")
    @Schema(description = "Pharmacological or therapeutic description", example = "Used to treat mild to moderate pain")
    private String description;

    @JsonProperty("active")
    @Schema(description = "Status record", example = "true")
    private Boolean active;
}
