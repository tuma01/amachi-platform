package com.amachi.app.vitalia.medicalcore.encounter.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.VisitTypeEnum;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

/**
 * Filtros de búsqueda dinámica ÉLITE para Encuentros Clínicos (FHIR).
 */
@Getter @Setter @SuperBuilder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public class EncounterSearchDto extends BaseSearchDto {
    private Long patientId;
    private Long doctorId;
    private Long episodeOfCareId;
    private Long appointmentId;
    private Long medicalHistoryId;
    private EncounterStatus status;
    private VisitTypeEnum encounterType;
    private OffsetDateTime encounterDateFrom;
    private OffsetDateTime encounterDateTo;
    private String triageLevel;
}
