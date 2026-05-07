package com.amachi.app.vitalia.medicalcore.nurse.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Personal de Enfermería")
public class NurseSearchDto extends BaseSearchDto {

    @Schema(description = "Búsqueda por nombre parcial")
    private String name;

    @Schema(description = "Número de licencia exacto", example = "ENF-BOL-00123")
    private String licenseNumber;

    @Schema(description = "ID de la unidad hospitalaria", example = "102")
    private Long departmentUnitId;

    @Schema(description = "Turno de trabajo")
    private ShiftEnum workShift;

    @Schema(description = "Rango o categoría de enfermería", example = "LICENCIADA")
    private String rank;
}
