package com.amachi.app.vitalia.medicalcore.prescription.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.encounter.repository.EncounterRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import com.amachi.app.vitalia.medicalcore.prescription.dto.PrescriptionDto;
import com.amachi.app.vitalia.medicalcore.prescription.dto.search.PrescriptionSearchDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import com.amachi.app.vitalia.medicalcore.prescription.mapper.PrescriptionMapper;
import com.amachi.app.vitalia.medicalcore.prescription.repository.PrescriptionRepository;
import com.amachi.app.vitalia.medicalcore.prescription.service.PrescriptionService;
import com.amachi.app.vitalia.medicalcore.prescription.specification.PrescriptionSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrescriptionServiceImpl extends BaseService<Prescription, PrescriptionDto, PrescriptionSearchDto>
        implements PrescriptionService {

    private final PrescriptionRepository repository;
    private final PrescriptionMapper mapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EncounterRepository encounterRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Prescription, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<Prescription> buildSpecification(PrescriptionSearchDto searchDto) {
        return new PrescriptionSpecification(searchDto);
    }

    @Override
    @Transactional
    public Prescription create(PrescriptionDto dto) {
        if (dto == null) throw new BusinessException("Prescription cannot be null");
        Long tenantId = TenantContext.getTenantId();

        Prescription entity = mapper.toEntity(dto);

        // Resolver Patient
        if (dto.getPatientId() == null) throw new BusinessException("Patient is required");
        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));

        // Resolver Doctor
        if (dto.getDoctorId() == null) throw new BusinessException("Doctor is required");
        if (!doctorRepository.existsByIdAndTenantId(dto.getDoctorId(), tenantId))
            throw new BusinessException("Doctor no encontrado con ID: " + dto.getDoctorId());
        entity.setDoctor(entityManager.getReference(Doctor.class, dto.getDoctorId()));

        // Resolver Encounter (opcional)
        if (dto.getEncounterId() != null) {
            if (!encounterRepository.existsByIdAndTenantId(dto.getEncounterId(), tenantId))
                throw new BusinessException("Encuentro no encontrado con ID: " + dto.getEncounterId());
            entity.setEncounter(entityManager.getReference(Encounter.class, dto.getEncounterId()));
        }

        if (entity.getAuthoredOn() == null) entity.setAuthoredOn(OffsetDateTime.now());

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(PrescriptionDto dto, Prescription existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected Prescription mapToEntity(PrescriptionDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(Prescription entity) { }

    @Override
    protected void publishUpdatedEvent(Prescription entity) { }
}
