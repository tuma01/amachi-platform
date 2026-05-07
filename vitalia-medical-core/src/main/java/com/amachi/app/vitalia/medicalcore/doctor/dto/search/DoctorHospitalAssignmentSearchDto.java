package com.amachi.app.vitalia.medicalcore.doctor.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Asignaciones Hospital-Médico")
public class DoctorHospitalAssignmentSearchDto extends BaseSearchDto {

    @Schema(description = "ID del médico", example = "2001")
    private Long doctorId;

    @Schema(description = "ID del hospital", example = "1")
    private Long hospitalId;

    @Schema(description = "Filtrar solo asignaciones primarias", example = "true")
    private Boolean isPrimary;

    @Schema(description = "Filtrar solo asignaciones vigentes (sin fecha de fin)", example = "true")
    private Boolean active;
}
