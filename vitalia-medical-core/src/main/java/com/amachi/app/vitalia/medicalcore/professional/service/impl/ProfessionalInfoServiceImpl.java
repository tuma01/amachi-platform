package com.amachi.app.vitalia.medicalcore.professional.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.vitalia.medicalcore.professional.dto.ProfessionalInfoDto;
import com.amachi.app.vitalia.medicalcore.professional.dto.search.ProfessionalInfoSearchDto;
import com.amachi.app.vitalia.medicalcore.professional.entity.ProfessionalInfo;
import com.amachi.app.vitalia.medicalcore.professional.mapper.ProfessionalInfoMapper;
import com.amachi.app.vitalia.medicalcore.professional.repository.ProfessionalInfoRepository;
import com.amachi.app.vitalia.medicalcore.professional.service.ProfessionalInfoService;
import com.amachi.app.vitalia.medicalcore.professional.specification.ProfessionalInfoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfessionalInfoServiceImpl extends BaseService<ProfessionalInfo, ProfessionalInfoDto, ProfessionalInfoSearchDto>
        implements ProfessionalInfoService {

    private final ProfessionalInfoRepository repository;
    private final ProfessionalInfoMapper mapper;
    private final PersonRepository personRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<ProfessionalInfo, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<ProfessionalInfo> buildSpecification(ProfessionalInfoSearchDto searchDto) {
        return new ProfessionalInfoSpecification(searchDto);
    }

    @Override
    @Transactional
    public ProfessionalInfo create(ProfessionalInfoDto dto) {
        if (dto == null) throw new BusinessException("ProfessionalInfo data cannot be null");

        ProfessionalInfo entity = mapper.toEntity(dto);

        if (dto.getPersonId() == null) throw new BusinessException("Person identity is required");
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
        entity.setPerson(person);

        // Si es la posición actual, marcar las anteriores como no actuales
        if (Boolean.TRUE.equals(dto.getIsCurrent())) {
            Long tenantId = TenantContext.getTenantId();
            repository.findByPersonIdAndIsCurrentTrueAndTenantId(dto.getPersonId(), tenantId)
                    .forEach(prev -> { prev.setIsCurrent(false); repository.save(prev); });
        }

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(ProfessionalInfoDto dto, ProfessionalInfo existing) {
        mapper.updateEntityFromDto(dto, existing);
        if (dto.getPersonId() != null && !dto.getPersonId().equals(existing.getPerson().getId())) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
            existing.setPerson(person);
        }
    }

    @Override
    protected ProfessionalInfo mapToEntity(ProfessionalInfoDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(ProfessionalInfo entity) { }

    @Override
    protected void publishUpdatedEvent(ProfessionalInfo entity) { }
}
