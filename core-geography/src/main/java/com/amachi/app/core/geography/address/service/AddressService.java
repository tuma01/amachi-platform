package com.amachi.app.core.geography.address.service;

import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.dto.search.AddressSearchDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.common.service.GenericService;

/**
 * Contrato de servicio para Direcciones (SaaS Elite Tier).
 */
public interface AddressService extends GenericService<Address, AddressDto, AddressSearchDto> {
}
