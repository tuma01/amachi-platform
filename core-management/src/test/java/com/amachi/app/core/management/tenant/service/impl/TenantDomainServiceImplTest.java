package com.amachi.app.core.management.tenant.service.impl;

import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.impl.AddressServiceImpl;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantDomainServiceImplTest extends AbstractTestSupport {

        @Mock
        private AddressServiceImpl addressService;

        @Mock
        private AddressMapper addressMapper;

        @Mock
        private com.amachi.app.core.management.theme.repository.ThemeRepository themeRepository;

        @InjectMocks
        private TenantDomainServiceImpl domainService;

        @Test
        void handleTenantAddress_WhenDtoHasAddressWithNoId_ThenCreateAddressAndSetId() {
                // Arrange
                Tenant entity = Instancio.create(Tenant.class);

                AddressDto addressDto = Instancio.of(AddressDto.class)
                        .set(field(AddressDto::getId), null)
                        .create();

                TenantDto dto = Instancio.of(TenantDto.class)
                        .set(field(TenantDto::getAddress), addressDto)
                        .create();

                Address createdAddress = Instancio.create(Address.class);
                Long expectedId = createdAddress.getId();

                // ✔ Solo esto es necesario
                when(addressService.create(any())).thenReturn(createdAddress);

                // Act
                domainService.handleTenantAddress(entity, dto);

                // Assert
                assertEquals(expectedId, entity.getAddressId());
                verify(addressService).create(any());
        }

        @Test
        void handleTenantAddress_WhenDtoHasAddressWithId_ThenSetExistingId() {
                // Arrange
                Tenant entity = Instancio.create(Tenant.class);
                Long existingId = 200L;

                AddressDto addressDto = Instancio.of(AddressDto.class)
                                .set(field(AddressDto::getId), existingId)
                                .create();

                TenantDto dto = Instancio.of(TenantDto.class)
                                .set(field(TenantDto::getAddress), addressDto)
                                .create();

                // Act
                domainService.handleTenantAddress(entity, dto);

                // Assert
                assertEquals(existingId, entity.getAddressId());
                verify(addressService, never()).create(any());
        }

        @Test
        void handleTenantAddress_WhenDtoHasNoAddress_ThenDoNothing() {
                // Arrange
                Tenant entity = Instancio.create(Tenant.class);
                // Ensure initial state is null to verify it stays null
                entity.setAddressId(null);

                TenantDto dto = Instancio.of(TenantDto.class)
                                .set(field(TenantDto::getAddress), null)
                                .create();

                // Act
                domainService.handleTenantAddress(entity, dto);

                // Assert
                assertNull(entity.getAddressId());
                verify(addressService, never()).create(any());
        }

        @Test
        void enrichTenantDto_WhenEntityHasAddressId_ThenEnrichDto() {
                // Arrange
                Long addressId = 50L;
                Tenant entity = Instancio.of(Tenant.class)
                                .set(field(Tenant::getAddressId), addressId)
                                .create();
                TenantDto dto = Instancio.create(TenantDto.class);

                Address foundAddress = Instancio.of(Address.class)
                                .set(field(Address::getId), addressId)
                                .create();

                AddressDto mappedDto = Instancio.of(AddressDto.class)
                                .set(field(AddressDto::getId), addressId)
                                .create();

                when(addressService.getById(addressId)).thenReturn(foundAddress);
                when(addressMapper.toDto(foundAddress)).thenReturn(mappedDto);

                // Act
                TenantDto result = domainService.enrichTenantDto(entity, dto);

                // Assert
                assertNotNull(result.getAddress());
                assertEquals(addressId, result.getAddress().getId());
        }

        @Test
        void handleTenantTheme_WhenThemeIdProvided_ThenSetTheme() {
                // Arrange
                Tenant entity = new Tenant();
                TenantDto dto = new TenantDto();
                dto.setThemeId(1L);

                com.amachi.app.core.domain.theme.entity.Theme theme = new com.amachi.app.core.domain.theme.entity.Theme();
                theme.setId(1L);

                when(themeRepository.findById(1L)).thenReturn(java.util.Optional.of(theme));

                // Act
                domainService.handleTenantTheme(entity, dto);

                // Assert
                assertEquals(theme, entity.getTheme());
                verify(themeRepository).findById(1L);
        }

        @Test
        void handleTenantTheme_WhenThemeIdIsNull_ThenSetThemeNull() {
                // Arrange
                Tenant entity = new Tenant();
                entity.setTheme(new com.amachi.app.core.domain.theme.entity.Theme());
                TenantDto dto = new TenantDto();
                dto.setThemeId(null);

                // Act
                domainService.handleTenantTheme(entity, dto);

                // Assert
                assertNull(entity.getTheme());
        }
}
