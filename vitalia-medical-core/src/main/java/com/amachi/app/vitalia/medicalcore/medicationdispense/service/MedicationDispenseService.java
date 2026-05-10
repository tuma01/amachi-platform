package com.amachi.app.vitalia.medicalcore.medicationdispense.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.MedicationDispenseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search.MedicationDispenseSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;

import java.util.List;

public interface MedicationDispenseService extends GenericService<MedicationDispense, MedicationDispenseDto, MedicationDispenseSearchDto> {

    List<MedicationDispense> getByPatient(Long patientId);

    List<MedicationDispense> getByPrescription(Long prescriptionId);
}
