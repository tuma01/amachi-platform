package com.amachi.app.vitalia.medicalcore.medicationadministration.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.MedicationAdministrationDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search.MedicationAdministrationSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;

import java.util.List;

public interface MedicationAdministrationService extends GenericService<MedicationAdministration, MedicationAdministrationDto, MedicationAdministrationSearchDto> {

    List<MedicationAdministration> getByPatient(Long patientId);

    List<MedicationAdministration> getByPrescription(Long prescriptionId);
}
