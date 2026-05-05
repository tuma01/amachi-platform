package com.amachi.app.vitalia.medicalcatalog.procedure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Medical Procedure Data Transfer Object (SaaS Elite Tier).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(name = "MedicalProcedure", description = "Schema to hold Medical Procedure or Laboratory Test information")
public class MedicalProcedureDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Size(max = 20)
    @Schema(description = "Procedure Code (CUPS, CPT, etc.)", example = "90.3.8.01")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Name {err.mandatory}")
    @Size(max = 500)
    @Schema(description = "Official procedure name", example = "HEMOGRAMA IV (HEMOGLOBINA, HEMATOCRITO...)")
    private String name;

    @JsonProperty("type")
    @NotBlank(message = "Type {err.mandatory}")
    @Schema(description = "Type of procedure", example = "LABORATORY")
    private String type;

    @JsonProperty("active")
    @Schema(description = "Status of the procedure", example = "true")
    private Boolean active;
}
