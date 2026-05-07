package com.amachi.app.vitalia.medicalcore.habit.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.habit.dto.PhysiologicalHabitDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.search.HabitSearchDto;
import com.amachi.app.vitalia.medicalcore.habit.entity.PhysiologicalHabit;
import com.amachi.app.vitalia.medicalcore.habit.mapper.PhysiologicalHabitMapper;
import com.amachi.app.vitalia.medicalcore.habit.repository.PhysiologicalHabitRepository;
import com.amachi.app.vitalia.medicalcore.habit.service.PhysiologicalHabitService;
import com.amachi.app.vitalia.medicalcore.habit.specification.HabitSpecification;
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
public class PhysiologicalHabitServiceImpl
        extends BaseService<PhysiologicalHabit, PhysiologicalHabitDto, HabitSearchDto>
        implements PhysiologicalHabitService {

    private final PhysiologicalHabitRepository repository;
    private final PhysiologicalHabitMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<PhysiologicalHabit, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<PhysiologicalHabit> buildSpecification(HabitSearchDto searchDto) {
        return new HabitSpecification<>(searchDto);
    }

    @Override
    @Transactional
    public PhysiologicalHabit create(PhysiologicalHabitDto dto) {
        if (dto == null) throw new BusinessException("PhysiologicalHabit cannot be null");
        Long tenantId = TenantContext.getTenantId();

        PhysiologicalHabit entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(PhysiologicalHabitDto dto, PhysiologicalHabit existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected PhysiologicalHabit mapToEntity(PhysiologicalHabitDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(PhysiologicalHabit entity) { }

    @Override
    protected void publishUpdatedEvent(PhysiologicalHabit entity) { }
}
