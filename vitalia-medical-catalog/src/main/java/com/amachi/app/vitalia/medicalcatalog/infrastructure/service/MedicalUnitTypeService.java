package com.amachi.app.vitalia.medicalcatalog.infrastructure.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.MedicalUnitTypeDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.search.MedicalUnitTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;

public interface MedicalUnitTypeService extends GenericService<MedicalUnitType, MedicalUnitTypeDto, MedicalUnitTypeSearchDto> {
}
