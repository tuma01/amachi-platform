package com.amachi.app.vitalia.medicalcatalog.infrastructure.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.MedicalUnitTypeDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.search.MedicalUnitTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.event.MedicalUnitTypeChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.mapper.MedicalUnitTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.repository.MedicalUnitTypeRepository;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.service.MedicalUnitTypeService;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.specification.MedicalUnitTypeSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Medical Unit Type Service Implementation.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class MedicalUnitTypeServiceImpl extends BaseService<MedicalUnitType, MedicalUnitTypeDto, MedicalUnitTypeSearchDto> implements MedicalUnitTypeService {

    private final MedicalUnitTypeRepository repository;
    private final MedicalUnitTypeMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<MedicalUnitType> buildSpecification(MedicalUnitTypeSearchDto searchDto) {
        return new MedicalUnitTypeSpecification(searchDto);
    }

    @Override
    protected MedicalUnitType mapToEntity(MedicalUnitTypeDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(MedicalUnitTypeDto dto, MedicalUnitType existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicalUnitType entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(MedicalUnitType entity) {
        publishChanged(entity);
    }

    private void publishChanged(MedicalUnitType entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new MedicalUnitTypeChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
