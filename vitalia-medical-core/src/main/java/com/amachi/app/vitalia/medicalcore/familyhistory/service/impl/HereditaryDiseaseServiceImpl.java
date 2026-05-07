package com.amachi.app.vitalia.medicalcore.familyhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.HereditaryDiseaseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.HereditaryDisease;
import com.amachi.app.vitalia.medicalcore.familyhistory.mapper.HereditaryDiseaseMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.repository.FamilyHistoryRepository;
import com.amachi.app.vitalia.medicalcore.familyhistory.repository.HereditaryDiseaseRepository;
import com.amachi.app.vitalia.medicalcore.familyhistory.service.HereditaryDiseaseService;
import com.amachi.app.vitalia.medicalcore.familyhistory.specification.FamilyHistorySpecification;
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
public class HereditaryDiseaseServiceImpl
        extends BaseService<HereditaryDisease, HereditaryDiseaseDto, FamilyHistorySearchDto>
        implements HereditaryDiseaseService {

    private final HereditaryDiseaseRepository repository;
    private final HereditaryDiseaseMapper mapper;
    private final FamilyHistoryRepository familyHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<HereditaryDisease, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<HereditaryDisease> buildSpecification(FamilyHistorySearchDto searchDto) {
        return new FamilyHistorySpecification<>(searchDto);
    }

    @Override
    @Transactional
    public HereditaryDisease create(HereditaryDiseaseDto dto) {
        if (dto == null) throw new BusinessException("HereditaryDisease cannot be null");
        Long tenantId = TenantContext.getTenantId();

        HereditaryDisease entity = mapper.toEntity(dto);

        if (dto.getFamilyHistoryId() == null) throw new BusinessException("FamilyHistory is required");
        if (!familyHistoryRepository.existsByIdAndTenantId(dto.getFamilyHistoryId(), tenantId))
            throw new BusinessException("Historial familiar no encontrado con ID: " + dto.getFamilyHistoryId());
        entity.setFamilyHistory(entityManager.getReference(FamilyHistory.class, dto.getFamilyHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(HereditaryDiseaseDto dto, HereditaryDisease existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected HereditaryDisease mapToEntity(HereditaryDiseaseDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(HereditaryDisease entity) { }

    @Override
    protected void publishUpdatedEvent(HereditaryDisease entity) { }
}
