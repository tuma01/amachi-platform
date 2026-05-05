package com.amachi.app.vitalia.medicalcatalog.allergy.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.AllergyDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.search.AllergySearchDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;

public interface AllergyService extends GenericService<Allergy, AllergyDto, AllergySearchDto> {
}
