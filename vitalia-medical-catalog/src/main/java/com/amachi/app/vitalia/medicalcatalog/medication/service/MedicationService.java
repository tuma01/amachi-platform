package com.amachi.app.vitalia.medicalcatalog.medication.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.MedicationDto;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.search.MedicationSearchDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;

public interface MedicationService extends GenericService<Medication, MedicationDto, MedicationSearchDto> {
}
