package com.amachi.app.vitalia.medicalcatalog.kinship.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.KinshipDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.search.KinshipSearchDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;
import com.amachi.app.vitalia.medicalcatalog.kinship.event.KinshipChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.kinship.mapper.KinshipMapper;
import com.amachi.app.vitalia.medicalcatalog.kinship.repository.KinshipRepository;
import com.amachi.app.vitalia.medicalcatalog.kinship.service.KinshipService;
import com.amachi.app.vitalia.medicalcatalog.kinship.specification.KinshipSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Kinship Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class KinshipServiceImpl extends BaseService<Kinship, KinshipDto, KinshipSearchDto> implements KinshipService {

    private final KinshipRepository repository;
    private final KinshipMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected Specification<Kinship> buildSpecification(KinshipSearchDto searchDto) {
        return new KinshipSpecification(searchDto);
    }

    @Override
    protected Kinship mapToEntity(KinshipDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(KinshipDto dto, Kinship existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public Kinship create(KinshipDto dto) {
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Kinship code '" + dto.getCode() + "' already exists in Global Catalog");
        }
        return super.create(dto);
    }

    @Override
    protected void publishCreatedEvent(Kinship entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(Kinship entity) {
        publishChanged(entity);
    }

    private void publishChanged(Kinship entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new KinshipChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
