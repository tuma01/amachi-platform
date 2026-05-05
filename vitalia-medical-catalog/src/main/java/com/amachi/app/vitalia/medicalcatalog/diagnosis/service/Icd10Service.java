package com.amachi.app.vitalia.medicalcatalog.diagnosis.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.Icd10Dto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.search.Icd10SearchDto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;

public interface Icd10Service extends GenericService<Icd10, Icd10Dto, Icd10SearchDto> {
}
