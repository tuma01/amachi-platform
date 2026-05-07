package com.amachi.app.vitalia.medicalcore.consultation.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.consultation.dto.ConsultationDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.search.ConsultationSearchDto;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;

import java.util.List;

public interface ConsultationService extends GenericService<Consultation, ConsultationDto, ConsultationSearchDto> {

    List<Consultation> getByPatientId(Long patientId);

    List<Consultation> getByMedicalHistoryId(Long medicalHistoryId);
}
