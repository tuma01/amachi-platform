package com.amachi.app.vitalia.medicalcore.hospitalization.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.DischargeMedicationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.DischargeMedicationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;

import java.util.List;

public interface DischargeMedicationService extends GenericService<DischargeMedication, DischargeMedicationDto, DischargeMedicationSearchDto> {

    List<DischargeMedication> getByHospitalizationId(Long hospitalizationId);
}
