package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.service.impl;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.search.HealthcareProviderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.mapper.HealthcareProviderMapper;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.repository.HealthcareProviderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import org.mockito.Spy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HealthcareProviderServiceImplTest extends AbstractTestSupport {

    @Mock
    private HealthcareProviderRepository repository;

    @Mock
    private HealthcareProviderMapper mapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    @Spy
    private HealthcareProviderServiceImpl service;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId(1L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void getAll_ShouldReturnList() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        when(repository.findAll()).thenReturn(List.of(entity));

        List<HealthcareProvider> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("SURA EPS");
    }

    @Test
    void getAllPaginated_ShouldReturnPage() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        HealthcareProviderSearchDto searchDto = new HealthcareProviderSearchDto();
        Page<HealthcareProvider> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<HealthcareProvider> result = service.getAll(searchDto, 0, 10);
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void getById_ShouldReturnEntity() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        HealthcareProvider result = service.getById(1L);

        assertThat(result.getName()).isEqualTo("SURA EPS");
    }

    @Test
    void create_ShouldSave() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        HealthcareProviderDto dto = new HealthcareProviderDto();
        dto.setCode("ABC123");
        dto.setTaxId("TAX123");

        when(repository.save(any())).thenReturn(entity);
        when(repository.existsByCode(anyString())).thenReturn(false);
        when(repository.existsByTaxId(anyString())).thenReturn(false);
        doReturn(entity).when(service).mapToEntity(dto);

        HealthcareProvider result = service.create(dto);

        assertThat(result.getName()).isEqualTo("SURA EPS");
        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    void update_ShouldSave() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        HealthcareProviderDto dto = mock(HealthcareProviderDto.class);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        doNothing().when(service).mergeEntities(eq(dto), any(HealthcareProvider.class));

        HealthcareProvider result = service.update(1L, dto);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void delete_ShouldCallRepo() {
        HealthcareProvider entity = loadJson("data/provider/provider-entity.json", HealthcareProvider.class);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository, times(1)).delete(entity);
    }
}
