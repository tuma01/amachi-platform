package com.amachi.app.core.geography.country.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.core.geography.country.dto.search.CountrySearchDto;
import com.amachi.app.core.geography.country.entity.Country;

/**
 * Contrato de servicio para Países (SaaS Elite Tier).
 */
public interface CountryService extends GenericService<Country, CountryDto, CountrySearchDto> {
}
