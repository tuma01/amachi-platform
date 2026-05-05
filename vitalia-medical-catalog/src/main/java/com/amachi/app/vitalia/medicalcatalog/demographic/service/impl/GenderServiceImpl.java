package com.amachi.app.vitalia.medicalcatalog.demographic.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.GenderDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.GenderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.Gender;
import com.amachi.app.vitalia.medicalcatalog.demographic.event.GenderChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.demographic.mapper.GenderMapper;
import com.amachi.app.vitalia.medicalcatalog.demographic.repository.GenderRepository;
import com.amachi.app.vitalia.medicalcatalog.demographic.service.GenderService;
import com.amachi.app.vitalia.medicalcatalog.demographic.specification.GenderSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Gender Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class GenderServiceImpl extends BaseService<Gender, GenderDto, GenderSearchDto> implements GenderService {

    private final GenderRepository repository;
    private final GenderMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<Gender> buildSpecification(GenderSearchDto searchDto) {
        return new GenderSpecification(searchDto);
    }

    @Override
    protected Gender mapToEntity(GenderDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(GenderDto dto, Gender existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public Gender create(GenderDto dto) {
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Gender code '" + dto.getCode() + "' already exists in Global Catalog");
        }
        return super.create(dto);
    }

    @Override
    protected void publishCreatedEvent(Gender entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(Gender entity) {
        publishChanged(entity);
    }

    private void publishChanged(Gender entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new GenderChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
