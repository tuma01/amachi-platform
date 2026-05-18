package com.amachi.app.vitalia.medicalcore.insurance.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.search.InsuranceSearchDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;

import java.util.List;

public interface InsuranceService extends GenericService<Insurance, InsuranceDto, InsuranceSearchDto> {
    List<Insurance> getInsurancesByPatient(Long patientId);
}
