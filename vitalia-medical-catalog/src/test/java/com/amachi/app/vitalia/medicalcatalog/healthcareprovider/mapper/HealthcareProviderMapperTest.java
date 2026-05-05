package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.mapper;

import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amachi.app.core.geography.address.mapper.AddressMapper;

@ExtendWith(MockitoExtension.class)
class HealthcareProviderMapperTest {
    private HealthcareProviderMapper mapper;
    
    @Mock
    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(HealthcareProviderMapper.class);
    }

    @Test
    void shouldMapDtoToEntity() {
        HealthcareProviderDto dto = Instancio.create(HealthcareProviderDto.class);
        HealthcareProvider entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getTaxId(), entity.getTaxId());
    }

    @Test
    void shouldMapEntityToDto() {
        HealthcareProvider entity = Instancio.create(HealthcareProvider.class);
        HealthcareProviderDto dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getTaxId(), dto.getTaxId());
    }
}
