package com.amachi.app.core.domain.tenant.dto;

import com.amachi.app.core.common.enums.TenantType;
import com.amachi.app.core.geography.address.dto.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Tenant", description = "Schema to hold Tenant information")
public class TenantDto {

    @Schema(description = "Id Tenant", example = "1")
    private Long id;

    @Schema(description = "Code of the Tenant", example = "TENANT001")
    private String code;

    @Schema(description = "Name of the Tenant", example = "Main Hospital")
    private String name;

    @Schema(description = "Type of the Tenant", example = "HOSPITAL")
    private TenantType type;// Ej: HOSPITAL, CLINIC, LABORATORIO, PHARMACY

    @Schema(description = "Indicates if the Tenant is active", example = "true")
    @Builder.Default
    private Boolean active = true;

    @Schema(description = "Description of the Tenant", example = "This is the main hospital tenant.")
    private String description;

    @Schema(description = "Theme ID associated with the Tenant")
    private Long themeId;

    @Schema(description = "Theme Name associated with the Tenant")
    private String themeName;

    @Schema(description = "Address of the Tenant")
    private AddressDto address;

    @Schema(description = "Primary Logo URL for branding", example = "https://cdn.vitalia.com/logos/tenant1.png")
    private String logoUrl;

    @Schema(description = "Favicon URL for branding", example = "https://cdn.vitalia.com/favicons/tenant1.ico")
    private String faviconUrl;
}
