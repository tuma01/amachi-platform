package com.amachi.app.vitalia.medicalcatalog.vaccine.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.VaccineDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.search.VaccineSearchDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.entity.Vaccine;

public interface VaccineService extends GenericService<Vaccine, VaccineDto, VaccineSearchDto> {
}
