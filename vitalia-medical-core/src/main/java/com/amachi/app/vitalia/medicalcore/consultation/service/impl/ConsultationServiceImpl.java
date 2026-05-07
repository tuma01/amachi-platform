package com.amachi.app.vitalia.medicalcore.consultation.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.consultation.dto.ConsultationDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.search.ConsultationSearchDto;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;
import com.amachi.app.vitalia.medicalcore.consultation.mapper.ConsultationMapper;
import com.amachi.app.vitalia.medicalcore.consultation.repository.ConsultationRepository;
import com.amachi.app.vitalia.medicalcore.consultation.service.ConsultationService;
import com.amachi.app.vitalia.medicalcore.consultation.specification.ConsultationSpecification;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.repository.MedicalHistoryRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
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
public class ConsultationServiceImpl
        extends BaseService<Consultation, ConsultationDto, ConsultationSearchDto>
        implements ConsultationService {

    private final ConsultationRepository repository;
    private final ConsultationMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Consultation, Long> getRepository() { return repository; }

    @Override
    protected Specification<Consultation> buildSpecification(ConsultationSearchDto s) {
        return new ConsultationSpecification(s);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Consultation entity) {}

    @Override
    protected void publishUpdatedEvent(Consultation entity) {}

    @Override
    protected Consultation mapToEntity(ConsultationDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(ConsultationDto dto, Consultation existing) {
        mapper.updateEntityFromDto(dto, existing);
        resolveRelations(dto, existing, TenantContext.getTenantId());
    }

    @Override
    @Transactional
    public Consultation create(ConsultationDto dto) {
        if (dto == null) throw new BusinessException("Consultation data cannot be null");
        Long tenantId = TenantContext.getTenantId();

        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        if (!medicalHistoryRepository.existsByIdAndTenantId(dto.getMedicalHistoryId(), tenantId))
            throw new BusinessException("Expediente clínico no encontrado con ID: " + dto.getMedicalHistoryId());

        Consultation entity = mapper.toEntity(dto);
        resolveRelations(dto, entity, tenantId);
        return repository.save(entity);
    }

    @Override
    public List<Consultation> getByPatientId(Long patientId) {
        return repository.findByPatientIdAndTenantId(patientId, TenantContext.getTenantId());
    }

    @Override
    public List<Consultation> getByMedicalHistoryId(Long medicalHistoryId) {
        return repository.findByMedicalHistoryIdAndTenantId(medicalHistoryId, TenantContext.getTenantId());
    }

    private void resolveRelations(ConsultationDto dto, Consultation entity, Long tenantId) {
        if (dto.getPatientId() != null) {
            entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));
        }
        if (dto.getDoctorId() != null) {
            if (!doctorRepository.existsByIdAndTenantId(dto.getDoctorId(), tenantId))
                throw new BusinessException("Médico no encontrado con ID: " + dto.getDoctorId());
            entity.setDoctor(entityManager.getReference(Doctor.class, dto.getDoctorId()));
        } else {
            entity.setDoctor(null);
        }
        if (dto.getMedicalHistoryId() != null) {
            entity.setMedicalHistory(entityManager.getReference(MedicalHistory.class, dto.getMedicalHistoryId()));
        }
    }
}
