package com.amachi.app.vitalia.medicalcatalog.identity.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.IdentificationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.search.IdentificationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.identity.entity.IdentificationType;
import com.amachi.app.vitalia.medicalcatalog.identity.event.IdentificationTypeChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.identity.mapper.IdentificationTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.identity.repository.IdentificationTypeRepository;
import com.amachi.app.vitalia.medicalcatalog.identity.service.IdentificationTypeService;
import com.amachi.app.vitalia.medicalcatalog.identity.specification.IdentificationTypeSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of IdentificationType Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class IdentificationTypeServiceImpl extends BaseService<IdentificationType, IdentificationTypeDto, IdentificationTypeSearchDto> implements IdentificationTypeService {

    private final IdentificationTypeRepository repository;
    private final IdentificationTypeMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<IdentificationType> buildSpecification(IdentificationTypeSearchDto searchDto) {
        return new IdentificationTypeSpecification(searchDto);
    }

    @Override
    protected IdentificationType mapToEntity(IdentificationTypeDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(IdentificationTypeDto dto, IdentificationType existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public IdentificationType create(IdentificationTypeDto dto) {
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Identification type code '" + dto.getCode() + "' already exists in Global Catalog");
        }
        return super.create(dto);
    }

    @Override
    protected void publishCreatedEvent(IdentificationType entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(IdentificationType entity) {
        publishChanged(entity);
    }

    private void publishChanged(IdentificationType entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new IdentificationTypeChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
