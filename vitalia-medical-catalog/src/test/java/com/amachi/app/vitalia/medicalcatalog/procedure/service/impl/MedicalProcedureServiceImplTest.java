package com.amachi.app.vitalia.medicalcatalog.procedure.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.search.MedicalProcedureSearchDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import com.amachi.app.vitalia.medicalcatalog.procedure.mapper.MedicalProcedureMapper;
import com.amachi.app.vitalia.medicalcatalog.procedure.repository.MedicalProcedureRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalProcedureServiceImplTest extends AbstractTestSupport {

    @Mock
    private MedicalProcedureRepository repository;

    @Mock
    private MedicalProcedureMapper mapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private MedicalProcedureServiceImpl service;

    @BeforeEach
    void setup() {
        service = spy(service);
    }

    @Test
    void getAll_ShouldReturnList() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        entity.setCode("90.3.8.01");
        when(repository.findAll()).thenReturn(List.of(entity));

        List<MedicalProcedure> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCode()).isEqualTo("90.3.8.01");
    }

    @Test
    void getAllPaginated_ShouldReturnPage() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        MedicalProcedureSearchDto searchDto = new MedicalProcedureSearchDto();
        Page<MedicalProcedure> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<MedicalProcedure> result = service.getAll(searchDto, 0, 10);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void getById_ShouldReturnEntity() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        entity.setCode("90.3.8.01");
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        MedicalProcedure result = service.getById(1L);

        assertThat(result.getCode()).isEqualTo("90.3.8.01");
    }

    @Test
    void create_ShouldSave() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        entity.setCode("90.3.8.01");
        MedicalProcedureDto dto = Instancio.create(MedicalProcedureDto.class);
        dto.setCode("90.3.8.01");
        when(repository.save(any())).thenReturn(entity);
        when(mapper.toEntity(any())).thenReturn(entity);

        MedicalProcedure result = service.create(dto);

        assertThat(result.getCode()).isEqualTo("90.3.8.01");
        verify(eventPublisher, times(1)).publish(any());
    }

    @Test
    void update_ShouldSave() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        entity.setId(1L);

        MedicalProcedureDto dto = Instancio.create(MedicalProcedureDto.class);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(MedicalProcedure.class))).thenReturn(entity);

        doNothing().when(service).mergeEntities(eq(dto), any(MedicalProcedure.class));
        doNothing().when(service).publishUpdatedEvent(any()); // 🔴 CLAVE

        MedicalProcedure result = service.update(1L, dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(repository).save(entity);
    }

    @Test
    void delete_ShouldCallRepoDelete() {
        MedicalProcedure entity = Instancio.create(MedicalProcedure.class);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(repository, times(1)).delete(entity);
    }
}
