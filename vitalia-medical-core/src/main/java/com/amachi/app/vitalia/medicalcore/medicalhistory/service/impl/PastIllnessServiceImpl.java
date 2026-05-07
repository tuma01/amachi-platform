package com.amachi.app.vitalia.medicalcore.medicalhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.PastIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.PastIllness;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.PastIllnessMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.PastIllnessRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.PastIllnessService;
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
public class PastIllnessServiceImpl
        extends BaseService<PastIllness, PastIllnessDto, IllnessSearchDto>
        implements PastIllnessService {

    private final PastIllnessRepository repository;
    private final PastIllnessMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<PastIllness, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<PastIllness> buildSpecification(IllnessSearchDto searchDto) {
        return new IllnessSpecification<>(searchDto);
    }

    @Override
    @Transactional
    public PastIllness create(PastIllnessDto dto) {
        if (dto == null) throw new BusinessException("PastIllness cannot be null");
        Long tenantId = TenantContext.getTenantId();

        PastIllness entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(PastIllnessDto dto, PastIllness existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected PastIllness mapToEntity(PastIllnessDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(PastIllness entity) { }

    @Override
    protected void publishUpdatedEvent(PastIllness entity) { }
}
