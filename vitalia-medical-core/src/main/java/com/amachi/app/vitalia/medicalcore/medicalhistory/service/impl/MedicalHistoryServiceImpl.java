package com.amachi.app.vitalia.medicalcore.medicalhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.MedicalHistoryDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.MedicalHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.MedicalHistoryMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.MedicalHistoryService;
import com.amachi.app.vitalia.medicalcore.medicalhistory.specification.MedicalHistorySpecification;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicalHistoryServiceImpl
        extends BaseService<MedicalHistory, MedicalHistoryDto, MedicalHistorySearchDto>
        implements MedicalHistoryService {

    private final MedicalHistoryRepository repository;
    private final MedicalHistoryMapper mapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<MedicalHistory, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<MedicalHistory> buildSpecification(MedicalHistorySearchDto searchDto) {
        return new MedicalHistorySpecification(searchDto);
    }

    @Override
    @Transactional
    public MedicalHistory create(MedicalHistoryDto dto) {
        if (dto == null) throw new BusinessException("MedicalHistory cannot be null");
        Long tenantId = TenantContext.getTenantId();

        MedicalHistory entity = mapper.toEntity(dto);

        // Resolver Patient
        if (dto.getPatientId() == null) throw new BusinessException("Patient is required");
        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));

        // Validar unicidad del historyNumber en el tenant
        if (repository.existsByHistoryNumberAndTenantId(entity.getHistoryNumber(), tenantId))
            throw new BusinessException("Ya existe un expediente con el número: " + entity.getHistoryNumber());

        // Si es el expediente actual, marcar los anteriores del mismo paciente como no-actuales
        if (Boolean.TRUE.equals(dto.getIsCurrent())) {
            repository.findByPatientIdAndTenantId(dto.getPatientId(), tenantId)
                    .forEach(prev -> { prev.setIsCurrent(false); repository.save(prev); });
        }

        // Resolver Doctor responsable (opcional)
        if (dto.getResponsibleDoctorId() != null) {
            if (!doctorRepository.existsByIdAndTenantId(dto.getResponsibleDoctorId(), tenantId))
                throw new BusinessException("Doctor no encontrado con ID: " + dto.getResponsibleDoctorId());
            entity.setResponsibleDoctor(entityManager.getReference(Doctor.class, dto.getResponsibleDoctorId()));
        }

        if (entity.getRecordDate() == null) entity.setRecordDate(LocalDate.now());

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(MedicalHistoryDto dto, MedicalHistory existing) {
        mapper.updateEntityFromDto(dto, existing);
        if (dto.getResponsibleDoctorId() != null) {
            Long tenantId = TenantContext.getTenantId();
            if (!doctorRepository.existsByIdAndTenantId(dto.getResponsibleDoctorId(), tenantId))
                throw new BusinessException("Doctor no encontrado con ID: " + dto.getResponsibleDoctorId());
            existing.setResponsibleDoctor(entityManager.getReference(Doctor.class, dto.getResponsibleDoctorId()));
        }
    }

    @Override
    protected MedicalHistory mapToEntity(MedicalHistoryDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(MedicalHistory entity) { }

    @Override
    protected void publishUpdatedEvent(MedicalHistory entity) { }
}
