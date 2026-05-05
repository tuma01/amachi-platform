package com.amachi.app.core.geography.address;

import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class AddressMapperTest {

    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        addressMapper = Mappers.getMapper(AddressMapper.class);
    }

    @Test
    void shouldMapAddressDtoToEntity() {
        AddressDto dto = Instancio.create(AddressDto.class);

        Address entity = addressMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getStreetName(), entity.getStreetName());
        assertEquals(dto.getCity(), entity.getCity());
        assertEquals(dto.getZipCode(), entity.getZipCode());
        // Geographic FK references (country, state, province, municipality) are resolved
        // by AddressServiceImpl via em.getReference(), not by the mapper itself.
        assertNull(entity.getCountry());
        assertNull(entity.getState());
        assertNull(entity.getProvince());
        assertNull(entity.getMunicipality());
    }

    @Test
    void shouldMapAddressToDto() {
        Address entity = Instancio.create(Address.class);
        AddressDto dto = addressMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getStreetName(), dto.getStreetName());
        assertEquals(entity.getCity(), dto.getCity());
        assertEquals(entity.getCountry().getId(), dto.getCountryId());
        assertEquals(entity.getMunicipality().getId(), dto.getMunicipalityId());
    }
}
