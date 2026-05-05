package com.amachi.app.core.geography.municipality.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.geography.municipality.dto.MunicipalityDto;
import com.amachi.app.core.geography.municipality.dto.search.MunicipalitySearchDto;
import com.amachi.app.core.geography.municipality.entity.Municipality;
import com.amachi.app.core.geography.municipality.event.MunicipalityCreatedEvent;
import com.amachi.app.core.geography.municipality.event.MunicipalityUpdatedEvent;
import com.amachi.app.core.geography.municipality.mapper.MunicipalityMapper;
import com.amachi.app.core.geography.municipality.repository.MunicipalityRepository;
import com.amachi.app.core.geography.municipality.service.MunicipalityService;
import com.amachi.app.core.geography.municipality.specification.MunicipalitySpecification;
import com.amachi.app.core.geography.province.entity.Province;
import com.amachi.app.core.geography.province.repository.ProvinceRepository;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.amachi.app.core.common.utils.AppConstants.ErrorMessages.ENTITY_MUST_NOT_BE_NULL;
import static java.util.Objects.requireNonNull;

/**
 * Motor de Gestión de Municipios (SaaS Elite Tier).
 * Elevado al estándar DTO-First de 3 genéricos.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MunicipalityServiceImpl extends BaseService<Municipality, MunicipalityDto, MunicipalitySearchDto> implements MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final ProvinceRepository provinceRepository;
    private final DomainEventPublisher eventPublisher;
    private final MunicipalityMapper mapper;

    @Override
    protected CommonRepository<Municipality, Long> getRepository() {
        return municipalityRepository;
    }

    @Override
    protected Specification<Municipality> buildSpecification(MunicipalitySearchDto searchDto) {
        return new MunicipalitySpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Municipality mapToEntity(MunicipalityDto dto) {
        Municipality entity = mapper.toEntity(dto);
        if (dto.getProvinceId() != null) {
            Province province = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException(Province.class.getName(), "error.resource.not.found", dto.getProvinceId()));
            entity.setProvince(province);
        }
        return entity;
    }

    @Override
    protected void mergeEntities(MunicipalityDto dto, Municipality existing) {
        mapper.updateEntityFromDto(dto, existing);
        if (dto.getProvinceId() != null) {
            Province province = provinceRepository.findById(dto.getProvinceId())
                    .orElseThrow(() -> new ResourceNotFoundException(Province.class.getName(), "error.resource.not.found", dto.getProvinceId()));
            existing.setProvince(province);
        }
    }

    @Override
    protected void publishCreatedEvent(Municipality entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new MunicipalityCreatedEvent(entity.getId(), entity.getName()));
        }
    }

    @Override
    protected void publishUpdatedEvent(Municipality entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(new MunicipalityUpdatedEvent(entity.getId(), entity.getName()));
        }
    }
}
