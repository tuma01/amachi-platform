package com.amachi.app.vitalia.medicalcatalog.allergy.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.search.AllergySearchDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import com.amachi.app.vitalia.medicalcatalog.allergy.repository.AllergyRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllergyServiceImplTest {

    @Mock
    private AllergyRepository repository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private AllergyServiceImpl service;

    private Allergy entity;

    @BeforeEach
    void setUp() {
        entity = new Allergy();
        entity.setId(1L);
        entity.setCode("ALL-001");
        entity.setName("Penicilina");
        entity.setType("DRUG");
        entity.setDescription("Alergia a la penicilina");
    }

    @Test
    void getAll_WithFilters_ShouldReturnPage() {
        AllergySearchDto searchDto = new AllergySearchDto();
        Page<Allergy> entityPage = new PageImpl<>(List.of(entity));

        when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(entityPage);

        Page<Allergy> result = service.getAll(searchDto, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_ShouldReturnEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Allergy result = service.getById(1L);

        assertNotNull(result);
        assertEquals("ALL-001", result.getCode());
    }
}
