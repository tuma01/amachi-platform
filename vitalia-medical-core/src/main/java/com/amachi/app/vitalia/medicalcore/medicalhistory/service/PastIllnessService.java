package com.amachi.app.vitalia.medicalcore.medicalhistory.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.PastIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.PastIllness;

public interface PastIllnessService extends GenericService<PastIllness, PastIllnessDto, IllnessSearchDto> {
}
