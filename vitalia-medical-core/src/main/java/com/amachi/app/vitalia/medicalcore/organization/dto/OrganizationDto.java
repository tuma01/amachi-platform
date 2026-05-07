package com.amachi.app.vitalia.medicalcore.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "Organization", description = "Organización externa o interna (Hospital, Clínica, Aseguradora)")
public class OrganizationDto {

    @Schema(description = "Identificador único interno (PK)", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "Nombre {err.mandatory}")
    @Size(max = 200)
    @Schema(description = "Nombre oficial de la organización", example = "CLÍNICA SAN JUAN S.A.")
    private String name;

    @Size(max = 50)
    @Schema(description = "NIT / RUC / Identificador legal único", example = "1234567890")
    private String legalIdentifier;

    @Size(max = 50)
    @Schema(description = "Tipo de organización (HOSPITAL, CLINIC, INSURANCE, LABORATORY)", example = "HOSPITAL")
    private String type;

    @Size(max = 250)
    @Schema(description = "Dirección física", example = "AV. ARCE 123, LA PAZ")
    private String address;

    @Size(max = 50)
    @Schema(description = "Teléfono de contacto", example = "+591-2-2123456")
    private String contactPhone;

    @Size(max = 150)
    @Schema(description = "Correo electrónico de contacto", example = "contacto@clinicasanjuan.com")
    private String email;

    @Size(max = 200)
    @Schema(description = "Sitio web", example = "https://www.clinicasanjuan.com")
    private String website;

    @Schema(description = "Indica si la organización está activa", example = "true")
    private Boolean active;
}
