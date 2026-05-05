package com.amachi.app.vitalia.medicalcatalog.demographic.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.CivilStatusDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.CivilStatusSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.CivilStatus;
import com.amachi.app.vitalia.medicalcatalog.demographic.event.CivilStatusChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.demographic.mapper.CivilStatusMapper;
import com.amachi.app.vitalia.medicalcatalog.demographic.repository.CivilStatusRepository;
import com.amachi.app.vitalia.medicalcatalog.demographic.service.CivilStatusService;
import com.amachi.app.vitalia.medicalcatalog.demographic.specification.CivilStatusSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CivilStatus Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class CivilStatusServiceImpl extends BaseService<CivilStatus, CivilStatusDto, CivilStatusSearchDto> implements CivilStatusService {

    private final CivilStatusRepository repository;
    private final CivilStatusMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<CivilStatus> buildSpecification(CivilStatusSearchDto searchDto) {
        return new CivilStatusSpecification(searchDto);
    }

    @Override
    protected CivilStatus mapToEntity(CivilStatusDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(CivilStatusDto dto, CivilStatus existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public CivilStatus create(CivilStatusDto dto) {
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Civil status code '" + dto.getCode() + "' already exists in Global Catalog");
        }
        return super.create(dto);
    }

    @Override
    protected void publishCreatedEvent(CivilStatus entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(CivilStatus entity) {
        publishChanged(entity);
    }

    private void publishChanged(CivilStatus entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new CivilStatusChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
