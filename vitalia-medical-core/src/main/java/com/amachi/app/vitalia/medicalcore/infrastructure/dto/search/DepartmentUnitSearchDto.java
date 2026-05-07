package com.amachi.app.vitalia.medicalcore.infrastructure.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Unidades Hospitalarias")
public class DepartmentUnitSearchDto extends BaseSearchDto {

    @Schema(description = "Filtrar por nombre parcial", example = "UCI")
    private String name;

    @Schema(description = "Filtrar por código exacto", example = "UCI-N3")
    private String code;

    @Schema(description = "Filtrar por piso", example = "3")
    private String floor;

    @Schema(description = "Solo unidades clínicas o solo administrativas")
    private Boolean isClinical;

    @Schema(description = "Solo unidades activas o inactivas")
    private Boolean active;

    @Schema(description = "ID de unidad padre para filtrar por jerarquía", example = "50")
    private Long parentUnitId;
}
