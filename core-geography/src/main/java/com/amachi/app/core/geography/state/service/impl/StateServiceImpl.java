package com.amachi.app.core.geography.state.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.geography.state.dto.StateDto;
import com.amachi.app.core.geography.state.dto.search.StateSearchDto;
import com.amachi.app.core.geography.state.entity.State;
import com.amachi.app.core.geography.state.event.StateCreatedEvent;
import com.amachi.app.core.geography.state.event.StateUpdatedEvent;
import com.amachi.app.core.geography.state.mapper.StateMapper;
import com.amachi.app.core.geography.state.repository.StateRepository;
import com.amachi.app.core.geography.state.service.StateService;
import com.amachi.app.core.geography.state.specification.StateSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor de Gestión de Estados (SaaS Elite Tier).
 * Elevado al estándar DTO-First de 3 genéricos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StateServiceImpl extends BaseService<State, StateDto, StateSearchDto> implements StateService {

    private final StateRepository stateRepository;
    private final DomainEventPublisher eventPublisher;
    private final StateMapper mapper;

    @Override
    protected CommonRepository<State, Long> getRepository() {
        return stateRepository;
    }

    @Override
    protected Specification<State> buildSpecification(StateSearchDto searchDto) {
        return new StateSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected State mapToEntity(StateDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(StateDto dto, State existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(State entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new StateCreatedEvent(entity.getId(), entity.getName()));
        }
    }

    @Override
    protected void publishUpdatedEvent(State entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new StateUpdatedEvent(entity.getId(), entity.getName()));
        }
    }
}
