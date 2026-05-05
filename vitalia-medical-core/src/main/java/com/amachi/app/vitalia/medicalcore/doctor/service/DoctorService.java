package com.amachi.app.vitalia.medicalcore.doctor.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;

/**
 * Interfaz de servicio para la gestion de medicos.
 */
public interface DoctorService extends GenericService<Doctor, DoctorDto, DoctorSearchDto> {
}
