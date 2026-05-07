package com.amachi.app.vitalia.medicalcore.medicalhistory.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.ActualIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.ActualIllness;

public interface ActualIllnessService extends GenericService<ActualIllness, ActualIllnessDto, IllnessSearchDto> {
}
