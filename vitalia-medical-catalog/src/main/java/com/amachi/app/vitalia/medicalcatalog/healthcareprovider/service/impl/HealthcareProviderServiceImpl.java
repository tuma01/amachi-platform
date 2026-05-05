package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.service.impl;

import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.HealthcareProviderDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.search.HealthcareProviderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.event.HealthcareProviderChangedEvent;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.mapper.HealthcareProviderMapper;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.repository.HealthcareProviderRepository;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.service.HealthcareProviderService;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.specification.HealthcareProviderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of HealthcareProvider Service following SaaS Elite Tier standards.
 * Patrón validado: DTO-First con hooks de mapeo.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HealthcareProviderServiceImpl extends BaseService<HealthcareProvider, HealthcareProviderDto, HealthcareProviderSearchDto> implements HealthcareProviderService {

    private final HealthcareProviderRepository repository;
    private final HealthcareProviderMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected com.amachi.app.core.common.repository.CommonRepository<HealthcareProvider, Long> getRepository() {
        return repository;
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected Specification<HealthcareProvider> buildSpecification(HealthcareProviderSearchDto searchDto) {
        return new HealthcareProviderSpecification(searchDto);
    }

    @Override
    protected HealthcareProvider mapToEntity(HealthcareProviderDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(HealthcareProviderDto dto, HealthcareProvider existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public HealthcareProvider create(HealthcareProviderDto dto) {
        requireNonNull(dto, "HealthcareProvider DTO must not be null");
        
        if (repository.existsByCode(dto.getCode().trim().toUpperCase())) {
            throw new BusinessException("Provider code '" + dto.getCode() + "' already exists in Global Catalog");
        }

        if (repository.existsByTaxId(dto.getTaxId().trim().toUpperCase())) {
            throw new BusinessException("Tax ID '" + dto.getTaxId() + "' already exists in Global Catalog");
        }

        return super.create(dto);
    }

    @Override
    protected void publishCreatedEvent(HealthcareProvider entity) {
        requireNonNull(entity, "Entity cannot be null for event publication");
        publishChanged(entity);
    }

    @Override
    protected void publishUpdatedEvent(HealthcareProvider entity) {
        publishChanged(entity);
    }

    private void publishChanged(HealthcareProvider entity) {
        if (eventPublisher != null) {
            eventPublisher.publish(
                    new HealthcareProviderChangedEvent(entity.getId(), entity.getCode(), entity.getName(), entity.getTaxId())
            );
        }
    }
}
