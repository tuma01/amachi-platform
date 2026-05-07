package com.amachi.app.core.domain.hospital.dto;

import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Schema(name = "Hospital", description = "Professional Hospital profile extending Tenant identity")
public class HospitalDto extends TenantDto {

    @Valid
    @JsonProperty
    @NotBlank(message = "Legal Name {err.required}")
    @Size(max = 200, message = "Legal Name {err.max_length}")
    @Schema(description = "Nombre legal de la institución", example = "Hospital San Rafael")
    private String legalName;

    @Valid
    @JsonProperty
    @NotBlank(message = "Tax ID {err.required}")
    @Size(max = 50, message = "Tax ID {err.max_length}")
    @Schema(description = "Identificador fiscal (NIT/RUC)", example = "123456789-0")
    private String taxId;

    @Valid
    @JsonProperty
    @NotBlank(message = "Medical License {err.required}")
    @Size(max = 100, message = "Medical License {err.max_length}")
    @Schema(description = "Licencia sanitaria principal", example = "LIC-998877")
    private String medicalLicense;

    @Valid
    @JsonProperty
    @Schema(description = "Teléfono de contacto principal", example = "+591 2 2441234")
    private String contactPhone;

    @Valid
    @JsonProperty
    @Email(message = "Email format {err.invalid}")
    @Schema(description = "Email oficial de contacto", example = "contacto@hospital.com")
    private String contactEmail;

    @Valid
    @JsonProperty
    @Schema(description = "Sitio web institucional", example = "https://www.hospital.com")
    private String website;

    // --- Pro Fields (Fase 13) ---
    
    @Valid
    @JsonProperty
    @Schema(description = "Nombre completo del Director Médico", example = "Dr. Alberto Amachi")
    private String medicalDirector;

    @Valid
    @JsonProperty
    @Schema(description = "Matrícula profesional del Director", example = "MD-776655")
    private String medicalDirectorLicense;

    @Valid
    @JsonProperty
    @Schema(description = "Categoría o nivel del hospital (e.g. Nivel III)", example = "III-1")
    private String hospitalCategory;

    @Valid
    @JsonProperty
    @Schema(description = "Capacidad total de camas", example = "250")
    private Integer bedCapacity;

    @Valid
    @JsonProperty
    @Schema(description = "Número de quirófanos activos", example = "12")
    private Integer operatingRoomsCount;

    @Valid
    @JsonProperty
    @Schema(description = "Disponibilidad de emergencias 24/7", example = "true")
    private Boolean emergency247;

    @Valid
    @JsonProperty
    @Schema(description = "Slogan o frase institucional", example = "Tecnología al servicio de la vida")
    private String slogan;

    @Valid
    @JsonProperty
    @Schema(description = "Número de Fax", example = "+591 2 2441235")
    private String faxNumber;

    @Valid
    @JsonProperty
    @Schema(description = "Número oficial de WhatsApp", example = "+591 77766554")
    private String whatsappNumber;

    @Valid
    @JsonProperty
    @Schema(description = "Enlaces a redes sociales (JSON)", example = "{\"twitter\": \"@hospital\"}")
    private String socialLinks;

    @Valid
    @JsonProperty
    @Schema(description = "URL del sello institucional oficial", example = "https://vitalia.com/seals/hosp1.png")
    private String sealUrl;
}
