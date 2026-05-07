package com.amachi.app.vitalia.medicalcore.observation.service.impl;

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
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.search.ObservationSearchDto;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import com.amachi.app.vitalia.medicalcore.observation.mapper.ObservationMapper;
import com.amachi.app.vitalia.medicalcore.observation.repository.ObservationRepository;
import com.amachi.app.vitalia.medicalcore.observation.service.ObservationService;
import com.amachi.app.vitalia.medicalcore.observation.specification.ObservationSpecification;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
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
public class ObservationServiceImpl extends BaseService<Observation, ObservationDto, ObservationSearchDto>
        implements ObservationService {

    private final ObservationRepository repository;
    private final ObservationMapper mapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EncounterRepository encounterRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Observation, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<Observation> buildSpecification(ObservationSearchDto searchDto) {
        return new ObservationSpecification(searchDto);
    }

    @Override
    @Transactional
    public Observation create(ObservationDto dto) {
        if (dto == null) throw new BusinessException("Observation cannot be null");
        Long tenantId = TenantContext.getTenantId();

        Observation entity = mapper.toEntity(dto);

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

        return repository.save(entity);
    }

    @Override
    protected Observation mapToEntity(ObservationDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(ObservationDto dto, Observation existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(Observation entity) { }

    @Override
    protected void publishUpdatedEvent(Observation entity) { }
}
