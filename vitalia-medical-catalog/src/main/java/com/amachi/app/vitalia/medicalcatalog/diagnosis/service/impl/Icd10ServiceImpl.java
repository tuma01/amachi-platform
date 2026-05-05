package com.amachi.app.vitalia.medicalcatalog.diagnosis.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.Icd10Dto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.search.Icd10SearchDto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.event.Icd10ChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.mapper.Icd10Mapper;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.repository.Icd10Repository;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.service.Icd10Service;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.specification.Icd10Specification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ICD-10 Service Implementation.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class Icd10ServiceImpl extends BaseService<Icd10, Icd10Dto, Icd10SearchDto> implements Icd10Service {

    private final Icd10Repository repository;
    private final Icd10Mapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<Icd10> buildSpecification(Icd10SearchDto searchDto) {
        return new Icd10Specification(searchDto);
    }

    @Override
    protected Icd10 mapToEntity(Icd10Dto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(Icd10Dto dto, Icd10 existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Icd10 entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(Icd10 entity) {
        publishChanged(entity);
    }

    private void publishChanged(Icd10 entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new Icd10ChangedEvent(entity.getId(), entity.getCode(), entity.getDescription())
            );
        }
    }
}
