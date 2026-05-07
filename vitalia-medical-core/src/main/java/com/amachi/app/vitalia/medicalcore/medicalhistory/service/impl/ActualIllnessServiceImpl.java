package com.amachi.app.vitalia.medicalcore.medicalhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.ActualIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.ActualIllness;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.ActualIllnessMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.ActualIllnessRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.ActualIllnessService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.specification.IllnessSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActualIllnessServiceImpl
        extends BaseService<ActualIllness, ActualIllnessDto, IllnessSearchDto>
        implements ActualIllnessService {

    private final ActualIllnessRepository repository;
    private final ActualIllnessMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<ActualIllness, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<ActualIllness> buildSpecification(IllnessSearchDto searchDto) {
        return new IllnessSpecification<>(searchDto);
    }

    @Override
    @Transactional
    public ActualIllness create(ActualIllnessDto dto) {
        if (dto == null) throw new BusinessException("ActualIllness cannot be null");
        Long tenantId = TenantContext.getTenantId();

        ActualIllness entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(ActualIllnessDto dto, ActualIllness existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected ActualIllness mapToEntity(ActualIllnessDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(ActualIllness entity) { }

    @Override
    protected void publishUpdatedEvent(ActualIllness entity) { }
}
