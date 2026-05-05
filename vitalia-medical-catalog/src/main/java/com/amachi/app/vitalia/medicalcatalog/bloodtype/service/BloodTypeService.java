package com.amachi.app.vitalia.medicalcatalog.bloodtype.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.BloodTypeDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.search.BloodTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.entity.BloodType;

public interface BloodTypeService extends GenericService<BloodType, BloodTypeDto, BloodTypeSearchDto> {
}
