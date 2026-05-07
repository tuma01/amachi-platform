package com.amachi.app.vitalia.medicalcore.infrastructure.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.RoomTypeEnum;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Habitaciones Hospitalarias")
public class RoomSearchDto extends BaseSearchDto {

    @Schema(description = "ID de la unidad hospitalaria", example = "101")
    private Long unitId;

    @Schema(description = "Número de habitación exacto o parcial", example = "301")
    private String roomNumber;

    @Schema(description = "Tipo de habitación")
    private RoomTypeEnum roomType;

    @Schema(description = "Estado de limpieza")
    private CleaningStatus cleaningStatus;

    @Schema(description = "Solo habitaciones privadas o compartidas")
    private Boolean privateRoom;

    @Schema(description = "Solo habitaciones activas o inactivas")
    private Boolean active;

    @Schema(description = "Código del ala o bloque hospitalario", example = "ALA-NORTE")
    private String blockCode;
}
