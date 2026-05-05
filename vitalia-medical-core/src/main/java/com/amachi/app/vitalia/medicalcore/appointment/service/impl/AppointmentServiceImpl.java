package com.amachi.app.vitalia.medicalcore.appointment.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.appointment.event.AppointmentCreatedEvent;
import com.amachi.app.vitalia.medicalcore.appointment.event.AppointmentUpdatedEvent;
import com.amachi.app.vitalia.medicalcore.appointment.mapper.AppointmentMapper;
import com.amachi.app.vitalia.medicalcore.appointment.repository.AppointmentRepository;
import com.amachi.app.vitalia.medicalcore.appointment.service.AppointmentService;
import com.amachi.app.vitalia.medicalcore.appointment.specification.AppointmentSpecification;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * Servicio clínico simplificado para validación de flujo end-to-end (Fase 1).
 * Implementa validaciones críticas de integridad y fechas sugeridas por auditoría.
 */
@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl extends BaseService<Appointment, AppointmentDto, AppointmentSearchDto> implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Appointment create(AppointmentDto dto) {
        if (dto == null) {
            throw new BusinessException("Appointment cannot be null");
        }

        Appointment appointment = mapper.toEntity(dto);
        return create(appointment);
    }

    @Transactional
    private Appointment create(Appointment appointment) {
        Long tenantId = TenantContext.getTenantId();

        // 1. Validación de cronología
        if (appointment.getStartTime() == null || appointment.getEndTime() == null) {
            throw new BusinessException("StartTime y EndTime son requeridos");
        }

        if (appointment.getStartTime().isAfter(appointment.getEndTime())) {
            throw new BusinessException("La hora de inicio debe ser anterior a la de fin");
        }

        // 2. Validaciones obligatorias
        if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
            throw new BusinessException("Patient is required");
        }

        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
            throw new BusinessException("Doctor is required");
        }

        // 3. Resolución segura de Patient
        Long patientId = appointment.getPatient().getId();

        if (!patientRepository.existsByIdAndTenantId(patientId, tenantId)) {
            throw new BusinessException("Paciente no encontrado con ID: " + patientId);
        }

        appointment.setPatient(entityManager.getReference(Patient.class, patientId));

        // 4. Resolución segura de Doctor + conflicto de agenda
        Long doctorId = appointment.getDoctor().getId();

        if (!doctorRepository.existsByIdAndTenantId(doctorId, tenantId)) {
            throw new BusinessException("Doctor no encontrado con ID: " + doctorId);
        }

        appointment.setDoctor(entityManager.getReference(Doctor.class, doctorId));

        boolean conflict = repository
                .existsByDoctorIdAndTenantIdAndStartTimeLessThanAndEndTimeGreaterThan(
                        doctorId,
                        tenantId,
                        appointment.getEndTime(),
                        appointment.getStartTime()
                );

        if (conflict) {
            throw new BusinessException("Doctor already has an appointment in this time range");
        }

        // 5. Estado inicial
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return repository.save(appointment);
    }

    @Override
    protected CommonRepository<Appointment, Long> getRepository() { return repository; }

    @Override
    protected Specification<Appointment> buildSpecification(AppointmentSearchDto searchDto) {
        return new AppointmentSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Appointment entity) {
        eventPublisher.publish(new AppointmentCreatedEvent(
                entity.getId(),
                entity.getDoctor() != null ? entity.getDoctor().getId() : null,
                entity.getPatient() != null ? entity.getPatient().getId() : null,
                entity.getStartTime()
        ));
    }

    @Override
    protected void publishUpdatedEvent(Appointment entity) {
        eventPublisher.publish(new AppointmentUpdatedEvent(
                entity.getId(),
                entity.getDoctor() != null ? entity.getDoctor().getId() : null,
                entity.getPatient() != null ? entity.getPatient().getId() : null,
                entity.getStartTime(),
                entity.getStatus()
        ));
    }

    @Override
    protected Appointment mapToEntity(AppointmentDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(AppointmentDto dto, Appointment existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public Appointment checkIn(Long id) {
        Appointment appointment = getById(id);

        if (appointment.getCheckedInAt() != null) {
            throw new BusinessException("appointment.already.checked.in");
        }

        appointment.setCheckedInAt(OffsetDateTime.now());
        appointment.setStatus(AppointmentStatus.IN_PROGRESS);

        return repository.save(appointment);
    }

    @Override
    @Transactional
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = getById(id);
        appointment.setStatus(status);
        return repository.save(appointment);
    }
}
