package com.amachi.app.core.geography.country.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.core.geography.country.dto.search.CountrySearchDto;
import com.amachi.app.core.geography.country.entity.Country;
import com.amachi.app.core.geography.country.event.CountryCreatedEvent;
import com.amachi.app.core.geography.country.event.CountryUpdatedEvent;
import com.amachi.app.core.geography.country.mapper.CountryMapper;
import com.amachi.app.core.geography.country.repository.CountryRepository;
import com.amachi.app.core.geography.country.service.CountryService;
import com.amachi.app.core.geography.country.specification.CountrySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor de Gestión de Países (SaaS Elite Tier).
 * Elevado al estándar DTO-First de 3 genéricos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryServiceImpl extends BaseService<Country, CountryDto, CountrySearchDto> implements CountryService {

    private final CountryRepository countryRepository;
    private final DomainEventPublisher eventPublisher;
    private final CountryMapper mapper;

    @Override
    protected CommonRepository<Country, Long> getRepository() {
        return countryRepository;
    }

    @Override
    protected Specification<Country> buildSpecification(CountrySearchDto searchDto) {
        return new CountrySpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Country mapToEntity(CountryDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(CountryDto dto, Country existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Country entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new CountryCreatedEvent(entity.getId(), entity.getName()));
        }
    }

    @Override
    protected void publishUpdatedEvent(Country entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new CountryUpdatedEvent(entity.getId(), entity.getName()));
        }
    }
}
