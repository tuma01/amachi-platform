package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.search.HealthcareProviderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;

public interface HealthcareProviderService extends GenericService<HealthcareProvider, HealthcareProviderDto, HealthcareProviderSearchDto> {
}
