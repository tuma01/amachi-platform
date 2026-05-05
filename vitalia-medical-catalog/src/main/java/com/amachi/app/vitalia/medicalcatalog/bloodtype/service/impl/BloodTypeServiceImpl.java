package com.amachi.app.vitalia.medicalcatalog.bloodtype.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import com.amachi.app.vitalia.medicalcatalog.allergy.event.AllergyChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.BloodTypeDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.search.BloodTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.entity.BloodType;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.event.BloodTypeChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.mapper.BloodTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.repository.BloodTypeRepository;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.service.BloodTypeService;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.specification.BloodTypeSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Blood Type Service Implementation.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class BloodTypeServiceImpl extends BaseService<BloodType, BloodTypeDto, BloodTypeSearchDto> implements BloodTypeService {

    private final BloodTypeRepository repository;
    private final BloodTypeMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<BloodType> buildSpecification(BloodTypeSearchDto searchDto) {
        return new BloodTypeSpecification(searchDto);
    }

    @Override
    protected BloodType mapToEntity(BloodTypeDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(BloodTypeDto dto, BloodType existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(BloodType entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(BloodType entity) {
        publishChanged(entity);
    }

    private void publishChanged(BloodType entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new BloodTypeChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
