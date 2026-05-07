package com.amachi.app.vitalia.medicalcore.familyhistory.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.FamilyHistoryDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;

public interface FamilyHistoryService extends GenericService<FamilyHistory, FamilyHistoryDto, FamilyHistorySearchDto> {
}
