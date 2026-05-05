package com.amachi.app.core.geography.province.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.geography.province.dto.ProvinceDto;
import com.amachi.app.core.geography.province.dto.search.ProvinceSearchDto;
import com.amachi.app.core.geography.province.entity.Province;
import com.amachi.app.core.geography.province.event.ProvinceCreatedEvent;
import com.amachi.app.core.geography.province.event.ProvinceUpdatedEvent;
import com.amachi.app.core.geography.province.mapper.ProvinceMapper;
import com.amachi.app.core.geography.province.repository.ProvinceRepository;
import com.amachi.app.core.geography.province.service.ProvinceService;
import com.amachi.app.core.geography.province.specification.ProvinceSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Motor de Gestión de Provincias (SaaS Elite Tier).
 * Elevado al estándar DTO-First de 3 genéricos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvinceServiceImpl extends BaseService<Province, ProvinceDto, ProvinceSearchDto> implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final DomainEventPublisher eventPublisher;
    private final ProvinceMapper mapper;

    @Override
    protected CommonRepository<Province, Long> getRepository() {
        return provinceRepository;
    }

    @Override
    protected Specification<Province> buildSpecification(ProvinceSearchDto searchDto) {
        return new ProvinceSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Province mapToEntity(ProvinceDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(ProvinceDto dto, Province existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Province entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new ProvinceCreatedEvent(entity.getId(), entity.getName()));
        }
    }

    @Override
    protected void publishUpdatedEvent(Province entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new ProvinceUpdatedEvent(entity.getId(), entity.getName()));
        }
    }
}
