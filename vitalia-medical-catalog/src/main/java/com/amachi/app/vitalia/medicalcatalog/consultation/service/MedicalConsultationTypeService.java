package com.amachi.app.vitalia.medicalcatalog.consultation.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.MedicalConsultationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.search.MedicalConsultationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;

public interface MedicalConsultationTypeService extends GenericService<MedicalConsultationType, MedicalConsultationTypeDto, MedicalConsultationTypeSearchDto> {
}
