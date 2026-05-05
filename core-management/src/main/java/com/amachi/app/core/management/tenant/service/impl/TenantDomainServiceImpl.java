package com.amachi.app.core.management.tenant.service.impl;

import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.impl.AddressServiceImpl;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TenantDomainServiceImpl {
    private final AddressServiceImpl addressService;
    private final AddressMapper addressMapper;
    private final com.amachi.app.core.management.theme.repository.ThemeRepository themeRepository;

    public void handleTenantAddress(Tenant entity, TenantDto dto) {
        if (dto.getAddress() == null) {
            return;
        }
        AddressDto addressDto = dto.getAddress();
        Long addressId;

        if (addressDto.getId() == null) {
            Address address = addressService.create(addressDto);
            addressId = address.getId();
        } else {
            addressId = addressDto.getId();
        }
        entity.setAddressId(addressId);
    }

    @Transactional
    public void handleTenantTheme(Tenant entity, TenantDto dto) {
        if (dto.getThemeId() != null) {
            // Only update if it's different to avoid version/proxy issues
            if (entity.getTheme() == null || !entity.getTheme().getId().equals(dto.getThemeId())) {
                entity.setTheme(themeRepository.findById(dto.getThemeId())
                        .orElseThrow(
                                () -> new com.amachi.app.core.common.exception.ResourceNotFoundException("Theme", "id",
                                        dto.getThemeId())));
            }
        } else {
            entity.setTheme(null);
        }
    }

    @Transactional(readOnly = true)
    public TenantDto enrichTenantDto(Tenant entity, TenantDto dto) {
        if (entity.getAddressId() != null) {
            AddressDto addressDto = addressMapper.toDto(
                    addressService.getById(entity.getAddressId()));
            dto.setAddress(addressDto);
        }
        return dto;
    }
}
