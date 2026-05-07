package com.amachi.app.vitalia.medicalcore.observation.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.search.ObservationSearchDto;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;

public interface ObservationService extends GenericService<Observation, ObservationDto, ObservationSearchDto> {
}
