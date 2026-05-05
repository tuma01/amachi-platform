package com.amachi.app.core.geography.province.service;

import com.amachi.app.core.geography.province.dto.ProvinceDto;
import com.amachi.app.core.geography.province.dto.search.ProvinceSearchDto;
import com.amachi.app.core.geography.province.entity.Province;
import com.amachi.app.core.common.service.GenericService;

/**
 * Contrato de servicio para Provincias (SaaS Elite Tier).
 */
public interface ProvinceService extends GenericService<Province, ProvinceDto, ProvinceSearchDto> {
}
