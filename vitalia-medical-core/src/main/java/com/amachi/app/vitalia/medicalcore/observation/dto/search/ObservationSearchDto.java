package com.amachi.app.vitalia.medicalcore.observation.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.ObservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Observaciones Clínicas")
public class ObservationSearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "ID del encuentro clínico", example = "12001")
    private Long encounterId;

    @Schema(description = "ID del médico que registró la observación", example = "2001")
    private Long doctorId;

    @Schema(description = "Código de observación (LOINC, etc.)", example = "8867-4")
    private String code;

    @Schema(description = "Estado de la observación", example = "FINAL")
    private ObservationStatus status;
}
