package com.amachi.app.vitalia.medicalcatalog.allergy.service.impl;

import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.AllergyDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.search.AllergySearchDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import com.amachi.app.vitalia.medicalcatalog.allergy.event.AllergyChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.allergy.mapper.AllergyMapper;
import com.amachi.app.vitalia.medicalcatalog.allergy.repository.AllergyRepository;
import com.amachi.app.vitalia.medicalcatalog.allergy.service.AllergyService;
import com.amachi.app.vitalia.medicalcatalog.allergy.specification.AllergySpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of Allergy Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Getter
@Transactional(readOnly = true)
public class AllergyServiceImpl extends BaseService<Allergy, AllergyDto, AllergySearchDto> implements AllergyService {

    private final AllergyRepository repository;
    private final AllergyMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<Allergy, Long> getRepository() {
        return repository;
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Specification<Allergy> buildSpecification(AllergySearchDto searchDto) {
        return new AllergySpecification(searchDto);
    }

    @Override
    protected Allergy mapToEntity(AllergyDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(AllergyDto dto, Allergy existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Allergy entity) {
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(Allergy entity) {
        publishChanged(entity);
    }

    private void publishChanged(Allergy entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new AllergyChangedEvent(entity.getId(), entity.getCode(), entity.getName())
            );
        }
    }
}
