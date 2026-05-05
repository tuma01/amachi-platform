package com.amachi.app.vitalia.medicalcore.patient.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.patient.dto.PatientDto;
import com.amachi.app.vitalia.medicalcore.patient.dto.search.PatientSearchDto;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;

/**
 * Interfaz de servicio central para la gestion de pacientes.
 */
public interface PatientService extends GenericService<Patient, PatientDto, PatientSearchDto> {
}
