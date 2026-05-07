package com.amachi.app.vitalia.medicalcore.familyhistory.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.HereditaryDiseaseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.HereditaryDisease;

public interface HereditaryDiseaseService extends GenericService<HereditaryDisease, HereditaryDiseaseDto, FamilyHistorySearchDto> {
}
