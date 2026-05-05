package com.amachi.app.vitalia.medicalcatalog.consultation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Medical Consultation Type Data Transfer Object (SaaS Elite Tier).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Schema(name = "MedicalConsultationType", description = "Schema to hold Medical Consultation Type information")
public class MedicalConsultationTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Size(max = 20)
    @Schema(description = "Consultation Type Code", example = "TELE")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Name {err.mandatory}")
    @Size(max = 150)
    @Schema(description = "Consultation Type Name", example = "Telemedicine")
    private String name;

    @JsonProperty("description")
    @Size(max = 500)
    @Schema(description = "Consultation Type Description", example = "Remote medical consultation via video call")
    private String description;

    @JsonProperty("specialtyId")
    @Schema(description = "Related Medical Specialty ID", example = "5")
    private Long specialtyId;

    @JsonProperty("specialtyName")
    @Schema(description = "Related Medical Specialty Name (Read-only)", example = "General Medicine", accessMode = Schema.AccessMode.READ_ONLY)
    private String specialtyName;

    @JsonProperty("active")
    @Schema(description = "Status of the consultation type record", example = "true")
    private Boolean active;
}
