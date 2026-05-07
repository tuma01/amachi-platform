package com.amachi.app.vitalia.medicalcore.hospitalization.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.DischargeMedicationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.DischargeMedicationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import com.amachi.app.vitalia.medicalcore.hospitalization.mapper.DischargeMedicationMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.repository.DischargeMedicationRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.repository.HospitalizationRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.service.DischargeMedicationService;
import com.amachi.app.vitalia.medicalcore.hospitalization.specification.DischargeMedicationSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DischargeMedicationServiceImpl
        extends BaseService<DischargeMedication, DischargeMedicationDto, DischargeMedicationSearchDto>
        implements DischargeMedicationService {

    private final DischargeMedicationRepository repository;
    private final DischargeMedicationMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final HospitalizationRepository hospitalizationRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<DischargeMedication, Long> getRepository() { return repository; }

    @Override
    protected Specification<DischargeMedication> buildSpecification(DischargeMedicationSearchDto s) {
        return new DischargeMedicationSpecification(s);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(DischargeMedication entity) {}

    @Override
    protected void publishUpdatedEvent(DischargeMedication entity) {}

    @Override
    protected DischargeMedication mapToEntity(DischargeMedicationDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(DischargeMedicationDto dto, DischargeMedication existing) {
        mapper.updateEntityFromDto(dto, existing);
        resolveRelations(dto, existing, TenantContext.getTenantId());
    }

    @Override
    @Transactional
    public DischargeMedication create(DischargeMedicationDto dto) {
        if (dto == null) throw new BusinessException("DischargeMedication data cannot be null");
        Long tenantId = TenantContext.getTenantId();

        if (!hospitalizationRepository.existsByIdAndTenantId(dto.getHospitalizationId(), tenantId))
            throw new BusinessException("Hospitalización no encontrada con ID: " + dto.getHospitalizationId());

        DischargeMedication entity = mapper.toEntity(dto);
        resolveRelations(dto, entity, tenantId);
        return repository.save(entity);
    }

    @Override
    public List<DischargeMedication> getByHospitalizationId(Long hospitalizationId) {
        return repository.findByHospitalizationIdAndTenantId(hospitalizationId, TenantContext.getTenantId());
    }

    private void resolveRelations(DischargeMedicationDto dto, DischargeMedication entity, Long tenantId) {
        if (dto.getHospitalizationId() != null) {
            entity.setHospitalization(entityManager.getReference(Hospitalization.class, dto.getHospitalizationId()));
        }
        if (dto.getMedicationId() != null) {
            entity.setMedication(entityManager.getReference(Medication.class, dto.getMedicationId()));
        } else {
            entity.setMedication(null);
        }
    }
}
