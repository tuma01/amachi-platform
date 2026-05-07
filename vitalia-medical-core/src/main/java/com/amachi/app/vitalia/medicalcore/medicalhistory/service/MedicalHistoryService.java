package com.amachi.app.vitalia.medicalcore.medicalhistory.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.MedicalHistoryDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.MedicalHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;

public interface MedicalHistoryService extends GenericService<MedicalHistory, MedicalHistoryDto, MedicalHistorySearchDto> {
}
