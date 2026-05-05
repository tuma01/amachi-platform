package com.amachi.app.vitalia.medicalcatalog.demographic.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.CivilStatusDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.CivilStatusSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.CivilStatus;

public interface CivilStatusService extends GenericService<CivilStatus, CivilStatusDto, CivilStatusSearchDto> {
}
