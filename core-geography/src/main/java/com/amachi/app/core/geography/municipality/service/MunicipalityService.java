package com.amachi.app.core.geography.municipality.service;

import com.amachi.app.core.geography.municipality.dto.MunicipalityDto;
import com.amachi.app.core.geography.municipality.dto.search.MunicipalitySearchDto;
import com.amachi.app.core.geography.municipality.entity.Municipality;
import com.amachi.app.core.common.service.GenericService;

/**
 * Contrato de servicio para Municipios (SaaS Elite Tier).
 */
public interface MunicipalityService extends GenericService<Municipality, MunicipalityDto, MunicipalitySearchDto> {
}
