package com.amachi.app.vitalia.medicalcore.nurse.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.nurse.dto.NurseDto;
import com.amachi.app.vitalia.medicalcore.nurse.dto.search.NurseSearchDto;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;

public interface NurseService extends GenericService<Nurse, NurseDto, NurseSearchDto> {
}
