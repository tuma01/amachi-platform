package com.amachi.app.vitalia.medicalcore.familyhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.FamilyHistoryDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;
import com.amachi.app.vitalia.medicalcore.familyhistory.mapper.FamilyHistoryMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.repository.FamilyHistoryRepository;
import com.amachi.app.vitalia.medicalcore.familyhistory.service.FamilyHistoryService;
import com.amachi.app.vitalia.medicalcore.familyhistory.specification.FamilyHistorySpecification;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
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
public class FamilyHistoryServiceImpl
        extends BaseService<FamilyHistory, FamilyHistoryDto, FamilyHistorySearchDto>
        implements FamilyHistoryService {

    private final FamilyHistoryRepository repository;
    private final FamilyHistoryMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<FamilyHistory, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<FamilyHistory> buildSpecification(FamilyHistorySearchDto searchDto) {
        return new FamilyHistorySpecification<>(searchDto);
    }

    @Override
    @Transactional
    public FamilyHistory create(FamilyHistoryDto dto) {
        if (dto == null) throw new BusinessException("FamilyHistory cannot be null");
        Long tenantId = TenantContext.getTenantId();

        FamilyHistory entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(FamilyHistoryDto dto, FamilyHistory existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected FamilyHistory mapToEntity(FamilyHistoryDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(FamilyHistory entity) { }

    @Override
    protected void publishUpdatedEvent(FamilyHistory entity) { }
}
