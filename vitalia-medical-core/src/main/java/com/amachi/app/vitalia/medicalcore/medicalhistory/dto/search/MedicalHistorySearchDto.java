package com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicalHistoryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Expedientes Clínicos")
public class MedicalHistorySearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "Número de historia clínica", example = "HCL-2026-00001")
    private String historyNumber;

    @Schema(description = "Estado del expediente", example = "ACTIVE")
    private MedicalHistoryStatus status;

    @Schema(description = "Filtrar solo el expediente vigente", example = "true")
    private Boolean isCurrent;
}
