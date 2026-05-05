package com.amachi.app.vitalia.medicalcatalog.allergy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;

/**
 * Allergy Data Transfer Object (SaaS Elite Tier).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(name = "Allergy", description = "Schema to hold Allergy information")
public class AllergyDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique Identifier", example = "1")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Schema(description = "Allergy Code", example = "DRUG-001")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Name {err.mandatory}")
    @Schema(description = "Allergy Name", example = "Penicillin")
    private String name;

    @JsonProperty("type")
    @NotBlank(message = "Type {err.mandatory}")
    @Schema(description = "Allergy Category/Type", example = "DRUG")
    private String type;

    @JsonProperty("description")
    @Schema(description = "Detailed Description", example = "Adverse allergic reaction to penicillin")
    private String description;
    
    @JsonProperty("criticality")
    @Schema(description = "Expected severity or criticality level (HIGH, MEDIUM, LOW)", example = "HIGH")
    private String criticality;

    @JsonProperty("active")
    @Schema(description = "Status of the allergy record", example = "true")
    private Boolean active;
}
