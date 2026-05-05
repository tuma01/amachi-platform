package com.amachi.app.vitalia.medicalcatalog.diagnosis.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.search.Icd10SearchDto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.Icd10Dto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.mapper.Icd10Mapper;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.repository.Icd10Repository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Icd10ServiceImplTest extends AbstractTestSupport {

    @Mock
    private Icd10Repository repository;

    @Mock
    private Icd10Mapper mapper;

    @Spy
    @InjectMocks
    private Icd10ServiceImpl service;

    @Test
    void getAll_ShouldReturnList() {
        Icd10 entity = new Icd10();
        entity.setCode("A00.0");
        when(repository.findAll()).thenReturn(List.of(entity));

        List<Icd10> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getCode()).isEqualTo("A00.0");
    }

    @Test
    void create_ShouldSaveEntity() {
        Icd10 entity = new Icd10();
        entity.setCode("A00.0");
        Icd10Dto dto = new Icd10Dto();
        dto.setCode("A00.0");

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(any())).thenReturn(entity);

        Icd10 result = service.create(dto);

        assertThat(result.getCode()).isEqualTo("A00.0");
        verify(repository).save(entity);
    }

    @Test
    void update_ShouldSaveEntity() {
        Icd10 entity = new Icd10();
        entity.setId(1L);
        Icd10Dto dto = new Icd10Dto();
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any())).thenReturn(entity);

        Icd10 result = service.update(1L, dto);

        assertThat(result.getId()).isEqualTo(1L);
        verify(mapper).updateEntityFromDto(dto, entity);
    }
}
