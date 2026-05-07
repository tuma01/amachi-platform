package com.amachi.app.vitalia.medicalcore.episodeofcare.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.EpisodeOfCareStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Episodios de Cuidado")
public class EpisodeOfCareSearchDto extends BaseSearchDto {

    @Schema(description = "ID del paciente", example = "5001")
    private Long patientId;

    @Schema(description = "ID del médico responsable", example = "2001")
    private Long managingDoctorId;

    @Schema(description = "Estado del episodio", example = "ACTIVE")
    private EpisodeOfCareStatus status;
}
