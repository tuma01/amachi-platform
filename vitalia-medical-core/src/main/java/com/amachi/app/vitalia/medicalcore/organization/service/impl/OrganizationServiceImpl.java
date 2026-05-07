package com.amachi.app.vitalia.medicalcore.organization.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.organization.dto.OrganizationDto;
import com.amachi.app.vitalia.medicalcore.organization.dto.search.OrganizationSearchDto;
import com.amachi.app.vitalia.medicalcore.organization.entity.Organization;
import com.amachi.app.vitalia.medicalcore.organization.mapper.OrganizationMapper;
import com.amachi.app.vitalia.medicalcore.organization.repository.OrganizationRepository;
import com.amachi.app.vitalia.medicalcore.organization.service.OrganizationService;
import com.amachi.app.vitalia.medicalcore.organization.specification.OrganizationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrganizationServiceImpl
        extends BaseService<Organization, OrganizationDto, OrganizationSearchDto>
        implements OrganizationService {

    private final OrganizationRepository repository;
    private final OrganizationMapper mapper;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<Organization, Long> getRepository() { return repository; }

    @Override
    protected Specification<Organization> buildSpecification(OrganizationSearchDto s) {
        return new OrganizationSpecification(s);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Organization entity) {}

    @Override
    protected void publishUpdatedEvent(Organization entity) {}

    @Override
    protected Organization mapToEntity(OrganizationDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(OrganizationDto dto, Organization existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public Organization create(OrganizationDto dto) {
        if (dto == null) throw new BusinessException("Organization data cannot be null");

        if (dto.getLegalIdentifier() != null) {
            Long tenantId = TenantContext.getTenantId();
            String normalizedId = dto.getLegalIdentifier().trim().toUpperCase();
            if (repository.existsByLegalIdentifierAndTenantId(normalizedId, tenantId)) {
                throw new BusinessException("Ya existe una organización con el identificador legal: " + dto.getLegalIdentifier());
            }
        }

        return repository.save(mapper.toEntity(dto));
    }
}
