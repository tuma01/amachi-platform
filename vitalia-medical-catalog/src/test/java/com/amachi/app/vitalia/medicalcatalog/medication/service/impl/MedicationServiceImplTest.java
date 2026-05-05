package com.amachi.app.vitalia.medicalcatalog.medication.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.MedicationDto;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.search.MedicationSearchDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcatalog.medication.mapper.MedicationMapper;
import com.amachi.app.vitalia.medicalcatalog.medication.repository.MedicationRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceImplTest extends AbstractTestSupport {

    @Mock
    private MedicationRepository repository;

    @Mock
    private MedicationMapper mapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private MedicationServiceImpl service;

    @BeforeEach
    void setup() {
        service = spy(service);
    }

    @Test
    void getAll_ShouldReturnList() {
        Medication entity = Instancio.create(Medication.class);
        entity.setGenericName("Paracetamol");
        when(repository.findAll()).thenReturn(List.of(entity));

        List<Medication> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getGenericName()).isEqualTo("Paracetamol");
    }

    @Test
    void getAllPaginated_ShouldReturnPage() {
        Medication entity = Instancio.create(Medication.class);
        MedicationSearchDto searchDto = Instancio.create(MedicationSearchDto.class);
        Page<Medication> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Medication> result = service.getAll(searchDto, 0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getById_ShouldReturnEntity() {
        Medication entity = Instancio.create(Medication.class);
        entity.setGenericName("Paracetamol");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Medication result = service.getById(1L);

        assertThat(result.getGenericName()).isEqualTo("Paracetamol");
    }

    @Test
    void create_ShouldSave() {
        MedicationDto dto = Instancio.create(MedicationDto.class);
        dto.setGenericName("Paracetamol");
        Medication entity = Instancio.create(Medication.class);
        entity.setGenericName("Paracetamol");
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toEntity(any())).thenReturn(entity);

        Medication result = service.create(dto);

        assertThat(result.getGenericName()).isEqualTo("Paracetamol");
        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    void update_ShouldSave() {
        MedicationDto dto = Instancio.create(MedicationDto.class);
        dto.setId(1L);
        Medication entity = Instancio.create(Medication.class);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);
        doNothing().when(service).mergeEntities(eq(dto), any(Medication.class));

        Medication result = service.update(1L, dto);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void delete_ShouldCallRepoDelete() {
        Medication entity = Instancio.create(Medication.class);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository, times(1)).delete(entity);
    }
}
