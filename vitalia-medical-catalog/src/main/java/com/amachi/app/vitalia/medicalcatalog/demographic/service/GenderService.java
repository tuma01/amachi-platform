package com.amachi.app.vitalia.medicalcatalog.demographic.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.GenderDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.GenderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.Gender;

public interface GenderService extends GenericService<Gender, GenderDto, GenderSearchDto> {
}
