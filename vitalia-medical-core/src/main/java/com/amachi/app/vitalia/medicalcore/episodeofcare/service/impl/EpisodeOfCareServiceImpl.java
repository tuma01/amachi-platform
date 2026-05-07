package com.amachi.app.vitalia.medicalcore.episodeofcare.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.EpisodeOfCareDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.dto.search.EpisodeOfCareSearchDto;
import com.amachi.app.vitalia.medicalcore.episodeofcare.entity.EpisodeOfCare;
import com.amachi.app.vitalia.medicalcore.episodeofcare.mapper.EpisodeOfCareMapper;
import com.amachi.app.vitalia.medicalcore.episodeofcare.repository.EpisodeOfCareRepository;
import com.amachi.app.vitalia.medicalcore.episodeofcare.service.EpisodeOfCareService;
import com.amachi.app.vitalia.medicalcore.episodeofcare.specification.EpisodeOfCareSpecification;
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
public class EpisodeOfCareServiceImpl extends BaseService<EpisodeOfCare, EpisodeOfCareDto, EpisodeOfCareSearchDto>
        implements EpisodeOfCareService {

    private final EpisodeOfCareRepository repository;
    private final EpisodeOfCareMapper mapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<EpisodeOfCare, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<EpisodeOfCare> buildSpecification(EpisodeOfCareSearchDto searchDto) {
        return new EpisodeOfCareSpecification(searchDto);
    }

    @Override
    @Transactional
    public EpisodeOfCare create(EpisodeOfCareDto dto) {
        if (dto == null) throw new BusinessException("EpisodeOfCare cannot be null");
        Long tenantId = TenantContext.getTenantId();

        EpisodeOfCare entity = mapper.toEntity(dto);

        // Resolver Patient
        if (dto.getPatientId() == null) throw new BusinessException("Patient is required");
        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));

        // Resolver Managing Doctor
        if (dto.getManagingDoctorId() == null) throw new BusinessException("Managing doctor is required");
        if (!doctorRepository.existsByIdAndTenantId(dto.getManagingDoctorId(), tenantId))
            throw new BusinessException("Doctor no encontrado con ID: " + dto.getManagingDoctorId());
        entity.setManagingDoctor(entityManager.getReference(Doctor.class, dto.getManagingDoctorId()));

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(EpisodeOfCareDto dto, EpisodeOfCare existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected EpisodeOfCare mapToEntity(EpisodeOfCareDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(EpisodeOfCare entity) { }

    @Override
    protected void publishUpdatedEvent(EpisodeOfCare entity) { }
}
