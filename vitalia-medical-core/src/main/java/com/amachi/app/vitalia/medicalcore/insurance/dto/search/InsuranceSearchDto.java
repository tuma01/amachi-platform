package com.amachi.app.vitalia.medicalcore.insurance.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Seguros Médicos")
public class InsuranceSearchDto extends BaseSearchDto {

    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "ID del proveedor de salud", example = "10")
    private Long providerId;

    @Schema(description = "Filtrar solo seguros vigentes", example = "true")
    private Boolean isCurrent;
}
