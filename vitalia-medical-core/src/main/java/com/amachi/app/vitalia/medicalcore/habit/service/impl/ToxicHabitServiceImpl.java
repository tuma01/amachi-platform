package com.amachi.app.vitalia.medicalcore.habit.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.habit.dto.ToxicHabitDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.search.HabitSearchDto;
import com.amachi.app.vitalia.medicalcore.habit.entity.ToxicHabit;
import com.amachi.app.vitalia.medicalcore.habit.mapper.ToxicHabitMapper;
import com.amachi.app.vitalia.medicalcore.habit.repository.ToxicHabitRepository;
import com.amachi.app.vitalia.medicalcore.habit.service.ToxicHabitService;
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
public class ToxicHabitServiceImpl
        extends BaseService<ToxicHabit, ToxicHabitDto, HabitSearchDto>
        implements ToxicHabitService {

    private final ToxicHabitRepository repository;
    private final ToxicHabitMapper mapper;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<ToxicHabit, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<ToxicHabit> buildSpecification(HabitSearchDto searchDto) {
        return new HabitSpecification<>(searchDto);
    }

    @Override
    @Transactional
    public ToxicHabit create(ToxicHabitDto dto) {
        if (dto == null) throw new BusinessException("ToxicHabit cannot be null");
        Long tenantId = TenantContext.getTenantId();

        ToxicHabit entity = mapper.toEntity(dto);

        if (dto.getMedicalHistoryId() == null) throw new BusinessException("MedicalHistory is required");
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente no encontrado con ID: " + dto.getMedicalHistoryId());
        entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(ToxicHabitDto dto, ToxicHabit existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected ToxicHabit mapToEntity(ToxicHabitDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(ToxicHabit entity) { }

    @Override
    protected void publishUpdatedEvent(ToxicHabit entity) { }
}
