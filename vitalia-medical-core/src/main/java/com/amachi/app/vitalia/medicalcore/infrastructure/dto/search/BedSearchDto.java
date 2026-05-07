package com.amachi.app.vitalia.medicalcore.infrastructure.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.BedStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Camas Hospitalarias")
public class BedSearchDto extends BaseSearchDto {

    @Schema(description = "ID de la habitación", example = "202")
    private Long roomId;

    @Schema(description = "Código exacto de cama", example = "UCI-N3-301A-01")
    private String bedCode;

    @Schema(description = "Estado operativo de la cama")
    private BedStatusEnum status;

    @Schema(description = "Filtrar solo camas ocupadas o disponibles")
    private Boolean isOccupied;

    @Schema(description = "Solo camas activas o inactivas")
    private Boolean active;
}
