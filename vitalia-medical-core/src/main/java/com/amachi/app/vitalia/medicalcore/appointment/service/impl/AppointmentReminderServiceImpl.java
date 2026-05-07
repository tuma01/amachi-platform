package com.amachi.app.vitalia.medicalcore.appointment.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentReminderDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentReminderSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;
import com.amachi.app.vitalia.medicalcore.appointment.mapper.AppointmentReminderMapper;
import com.amachi.app.vitalia.medicalcore.appointment.repository.AppointmentReminderRepository;
import com.amachi.app.vitalia.medicalcore.appointment.repository.AppointmentRepository;
import com.amachi.app.vitalia.medicalcore.appointment.service.AppointmentReminderService;
import com.amachi.app.vitalia.medicalcore.appointment.specification.AppointmentReminderSpecification;
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
public class AppointmentReminderServiceImpl
        extends BaseService<AppointmentReminder, AppointmentReminderDto, AppointmentReminderSearchDto>
        implements AppointmentReminderService {

    private final AppointmentReminderRepository repository;
    private final AppointmentReminderMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<AppointmentReminder, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<AppointmentReminder> buildSpecification(AppointmentReminderSearchDto searchDto) {
        return new AppointmentReminderSpecification(searchDto);
    }

    @Override
    @Transactional
    public AppointmentReminder create(AppointmentReminderDto dto) {
        if (dto == null) throw new BusinessException("AppointmentReminder cannot be null");
        Long tenantId = TenantContext.getTenantId();

        AppointmentReminder entity = mapper.toEntity(dto);

        if (dto.getAppointmentId() == null) throw new BusinessException("Appointment is required");
        if (!appointmentRepository.existsByIdAndTenantId(dto.getAppointmentId(), tenantId))
            throw new BusinessException("Cita no encontrada con ID: " + dto.getAppointmentId());
        entity.setAppointment(entityManager.getReference(Appointment.class, dto.getAppointmentId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(AppointmentReminderDto dto, AppointmentReminder existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected AppointmentReminder mapToEntity(AppointmentReminderDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(AppointmentReminder entity) { }

    @Override
    protected void publishUpdatedEvent(AppointmentReminder entity) { }
}
