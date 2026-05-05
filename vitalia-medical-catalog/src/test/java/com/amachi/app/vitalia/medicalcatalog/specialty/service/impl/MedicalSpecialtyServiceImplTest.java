package com.amachi.app.vitalia.medicalcatalog.specialty.service.impl;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import com.amachi.app.vitalia.medicalcatalog.specialty.dto.MedicalSpecialtyDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.dto.search.MedicalSpecialtySearchDto;
import com.amachi.app.vitalia.medicalcatalog.specialty.entity.MedicalSpecialty;
import com.amachi.app.vitalia.medicalcatalog.specialty.mapper.MedicalSpecialtyMapper;
import com.amachi.app.vitalia.medicalcatalog.specialty.repository.MedicalSpecialtyRepository;
import org.instancio.Instancio;
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

import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalSpecialtyServiceImplTest extends AbstractTestSupport {

    @Mock
    private MedicalSpecialtyRepository repository;

    @Mock
    private MedicalSpecialtyMapper mapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private MedicalSpecialtyServiceImpl service;

    @BeforeEach
    void setup() {
        service = spy(service);
    }

    @Test
    void getAll_ShouldReturnList() {
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setName("Cardiología");
        when(repository.findAll()).thenReturn(List.of(entity));

        List<MedicalSpecialty> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Cardiología");
    }

    @Test
    void getAllPaginated_ShouldReturnPage() {
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setName("Cardiología");
        MedicalSpecialtySearchDto searchDto = new MedicalSpecialtySearchDto();
        Page<MedicalSpecialty> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicalSpecialty> result = service.getAll(searchDto, 0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getById_ShouldReturnEntity() {
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setName("Cardiología");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        MedicalSpecialty result = service.getById(1L);

        assertThat(result.getName()).isEqualTo("Cardiología");
    }

    @Test
    void create_ShouldSave() {
        MedicalSpecialtyDto dto = Instancio.create(MedicalSpecialtyDto.class);
        dto.setName("Cardiología");
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setName("Cardiología");

        when(repository.save(any())).thenReturn(entity);
        when(mapper.toEntity(any())).thenReturn(entity);

        MedicalSpecialty result = service.create(dto);

        assertThat(result.getName()).isEqualTo("Cardiología");
        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    void update_ShouldSave() {
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setId(1L);
        MedicalSpecialtyDto dto = Instancio.create(MedicalSpecialtyDto.class);
        dto.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        doNothing().when(service).mergeEntities(eq(dto), any(MedicalSpecialty.class));

        MedicalSpecialty result = service.update(1L, dto);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void delete_ShouldCallRepoDelete() {
        MedicalSpecialty entity = Instancio.create(MedicalSpecialty.class);
        entity.setId(1L);

        when(repository.findById(1L))
                .thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository).delete(entity);
    }
}
