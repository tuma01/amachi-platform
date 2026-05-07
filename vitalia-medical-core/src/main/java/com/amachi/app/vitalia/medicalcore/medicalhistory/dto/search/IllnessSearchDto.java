package com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Enfermedades (Actual/Pasada)")
public class IllnessSearchDto extends BaseSearchDto {

    @Schema(description = "ID del expediente clínico", example = "101")
    private Long medicalHistoryId;

    @Schema(description = "Nombre parcial de la enfermedad", example = "Hyper")
    private String name;
}
