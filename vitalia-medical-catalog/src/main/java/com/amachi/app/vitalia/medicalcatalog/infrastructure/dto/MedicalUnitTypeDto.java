package com.amachi.app.vitalia.medicalcatalog.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Medical Unit Type Data Transfer Object (SaaS Elite Tier).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(name = "MedicalUnitType", description = "Schema to hold Medical Unit Type information")
public class MedicalUnitTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Size(max = 20)
    @Schema(description = "Unit Type Code", example = "ICU")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Name {err.mandatory}")
    @Size(max = 150)
    @Schema(description = "Unit Type Name", example = "Intensive Care Unit")
    private String name;

    @JsonProperty("description")
    @Size(max = 500)
    @Schema(description = "Unit Type Description", example = "Specialized unit for patients with life-threatening illnesses")
    private String description;

    @JsonProperty("active")
    @Schema(description = "Status of the unit type record", example = "true")
    private Boolean active;
}
