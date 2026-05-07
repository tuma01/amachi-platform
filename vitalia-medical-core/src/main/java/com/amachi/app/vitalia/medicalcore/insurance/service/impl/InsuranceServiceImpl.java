package com.amachi.app.vitalia.medicalcore.insurance.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.search.InsuranceSearchDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import com.amachi.app.vitalia.medicalcore.insurance.mapper.InsuranceMapper;
import com.amachi.app.vitalia.medicalcore.insurance.repository.InsuranceRepository;
import com.amachi.app.vitalia.medicalcore.insurance.service.InsuranceService;
import com.amachi.app.vitalia.medicalcore.insurance.specification.InsuranceSpecification;
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
public class InsuranceServiceImpl
        extends BaseService<Insurance, InsuranceDto, InsuranceSearchDto>
        implements InsuranceService {

    private final InsuranceRepository repository;
    private final InsuranceMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Insurance, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<Insurance> buildSpecification(InsuranceSearchDto searchDto) {
        return new InsuranceSpecification(searchDto);
    }

    @Override
    @Transactional
    public Insurance create(InsuranceDto dto) {
        if (dto == null) throw new BusinessException("Insurance cannot be null");
        Long tenantId = TenantContext.getTenantId();

        Insurance entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        // Si es el seguro vigente, marcar los anteriores del mismo expediente como no-vigentes
        if (Boolean.TRUE.equals(dto.getIsCurrent())) {
            repository.findByMedicalHistoryIdAndIsCurrentTrueAndTenantId(dto.getMedicalHistoryId(), tenantId)
                    .forEach(prev -> { prev.setIsCurrent(false); repository.save(prev); });
        }

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(InsuranceDto dto, Insurance existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected Insurance mapToEntity(InsuranceDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(Insurance entity) { }

    @Override
    protected void publishUpdatedEvent(Insurance entity) { }
}
