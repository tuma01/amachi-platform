package com.amachi.app.vitalia.medicalcore.familyhistory.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Historial Familiar")
public class FamilyHistorySearchDto extends BaseSearchDto {

    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "Filtrar solo el vigente", example = "true")
    private Boolean isCurrent;
}
