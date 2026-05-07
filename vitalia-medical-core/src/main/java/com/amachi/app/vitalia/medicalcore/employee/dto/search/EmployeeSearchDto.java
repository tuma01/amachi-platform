package com.amachi.app.vitalia.medicalcore.employee.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.EmployeeStatus;
import com.amachi.app.core.common.enums.EmployeeType;
import com.amachi.app.core.common.enums.ShiftEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Personal Hospitalario")
public class EmployeeSearchDto extends BaseSearchDto {

    @Schema(description = "Búsqueda por nombre parcial del empleado")
    private String name;

    @Schema(description = "Código exacto del empleado", example = "EMP-2024-001")
    private String employeeCode;

    @Schema(description = "Tipo de personal")
    private EmployeeType employeeType;

    @Schema(description = "Estado laboral")
    private EmployeeStatus employeeStatus;

    @Schema(description = "ID de la unidad hospitalaria", example = "101")
    private Long departmentUnitId;

    @Schema(description = "Turno de trabajo")
    private ShiftEnum workShift;
}
