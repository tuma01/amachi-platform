package com.amachi.app.vitalia.medicalcore.prescription.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.prescription.dto.PrescriptionDto;
import com.amachi.app.vitalia.medicalcore.prescription.dto.search.PrescriptionSearchDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;

public interface PrescriptionService extends GenericService<Prescription, PrescriptionDto, PrescriptionSearchDto> {
}
