package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto;

import com.amachi.app.core.geography.address.dto.AddressDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * Healthcare Provider (Insurance/Payer) Data Transfer Object (SaaS Elite Tier).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Schema(name = "HealthcareProvider", description = "Schema to hold Healthcare Provider information")
public class HealthcareProviderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    @Schema(description = "Unique identifier", example = "5")
    private Long id;

    @JsonProperty("code")
    @NotBlank(message = "Code {err.mandatory}")
    @Size(max = 20)
    @Schema(description = "Standardized alphanumeric code", example = "EPS-001")
    private String code;

    @JsonProperty("name")
    @NotBlank(message = "Name {err.mandatory}")
    @Size(max = 250)
    @Schema(description = "Official trade name or legal entity name", example = "SURA EPS")
    private String name;

    @JsonProperty("taxId")
    @NotBlank(message = "Tax ID {err.mandatory}")
    @Size(max = 50)
    @Schema(description = "Official tax identification (NIT, RUC, VAT)", example = "860066942-7")
    private String taxId;

    @JsonProperty("officialEmail")
    @Email(message = "Format {err.invalid}")
    @Schema(description = "Institutional email address", example = "claims@sura.com")
    private String officialEmail;

    @JsonProperty("officialPhone")
    @Schema(description = "Institutional phone number", example = "+576013000000")
    private String officialPhone;

    @JsonProperty("emergencyPhone")
    @Schema(description = "24/7 emergency assistance line", example = "+57018000519111")
    private String emergencyPhone;

    @JsonProperty("website")
    @Schema(description = "Institutional website portal", example = "https://www.segurossura.com.co")
    private String website;

    @JsonProperty("logoUrl")
    @Schema(description = "Public URL for institutional logo", example = "/assets/catalog/logos/sura.png")
    private String logoUrl;

    @NotNull(message = "Province {err.mandatory}")
    @Schema(description = "ID of the headquarters address record", example = "10")
    private Long hqAddressId;

    @JsonProperty("active")
    @Schema(description = "Status of the record", example = "true")
    private Boolean active;
}
