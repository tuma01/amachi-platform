package com.amachi.app.vitalia.medicalcore.hospitalization.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.HospitalizationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.HospitalizationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;

import java.util.List;

public interface HospitalizationService extends GenericService<Hospitalization, HospitalizationDto, HospitalizationSearchDto> {

    Hospitalization dischargePatient(Long id, String dischargeSummary);

    List<Hospitalization> getByPatientId(Long patientId);
}
