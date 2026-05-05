package com.amachi.app.vitalia.medicalcatalog.vaccine.service.impl;
 
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;

import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.VaccineDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.search.VaccineSearchDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.entity.Vaccine;
import com.amachi.app.vitalia.medicalcatalog.vaccine.event.VaccineCreatedEvent;
import com.amachi.app.vitalia.medicalcatalog.vaccine.mapper.VaccineMapper;
import com.amachi.app.vitalia.medicalcatalog.vaccine.repository.VaccineRepository;
import com.amachi.app.vitalia.medicalcatalog.vaccine.service.VaccineService;
import com.amachi.app.vitalia.medicalcatalog.vaccine.specification.VaccineSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
/**
 * Implementation of Vaccine Service following SaaS Elite Tier standards.
 * Pure Global Catalog (No Tenant Isolation).
 */
@Service
@RequiredArgsConstructor
@Getter
public class VaccineServiceImpl extends BaseService<Vaccine, VaccineDto, VaccineSearchDto> implements VaccineService {
 
    private final VaccineRepository repository;
    private final DomainEventPublisher eventPublisher;
    private final VaccineMapper mapper;
 
    @Override
    protected Specification<Vaccine> buildSpecification(VaccineSearchDto searchDto) {
        return new VaccineSpecification(searchDto);
    }

    @Override
    protected Vaccine mapToEntity(VaccineDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(VaccineDto dto, Vaccine existing) {
        mapper.updateEntityFromDto(dto, existing);
    }
 
    @Override
    @Transactional
    public Vaccine create(VaccineDto dto) {
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Vaccine code '" + dto.getCode() + "' already exists in Global Catalog");
        }
        return super.create(dto);
    }

    @Override
    @Transactional
    public Vaccine update(Long id, VaccineDto dto) {
        return super.update(id, dto);
    }
 
    @Override
    protected void publishCreatedEvent(Vaccine entity) {
        eventPublisher.publish(new VaccineCreatedEvent(entity.getId(), entity.getCode(), entity.getName()));
    }
 
    @Override
    protected void publishUpdatedEvent(Vaccine entity) {
        // En este catálogo solo se publica evento al crear
    }
}
