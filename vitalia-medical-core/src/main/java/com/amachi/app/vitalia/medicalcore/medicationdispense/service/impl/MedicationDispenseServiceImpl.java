package com.amachi.app.vitalia.medicalcore.medicationdispense.service.impl;

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
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.MedicationDispenseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search.MedicationDispenseSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import com.amachi.app.vitalia.medicalcore.medicationdispense.mapper.MedicationDispenseMapper;
import com.amachi.app.vitalia.medicalcore.medicationdispense.repository.MedicationDispenseRepository;
import com.amachi.app.vitalia.medicalcore.medicationdispense.service.MedicationDispenseService;
import com.amachi.app.vitalia.medicalcore.medicationdispense.specification.MedicationDispenseSpecification;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import com.amachi.app.vitalia.medicalcore.prescription.repository.PrescriptionRepository;
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
public class MedicationDispenseServiceImpl
        extends BaseService<MedicationDispense, MedicationDispenseDto, MedicationDispenseSearchDto>
        implements MedicationDispenseService {

    private final MedicationDispenseRepository repository;
    private final MedicationDispenseMapper mapper;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final EncounterRepository encounterRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<MedicationDispense, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<MedicationDispense> buildSpecification(MedicationDispenseSearchDto searchDto) {
        return new MedicationDispenseSpecification(searchDto);
    }

    @Override
    @Transactional
    public MedicationDispense create(MedicationDispenseDto dto) {
        if (dto == null) throw new BusinessException("MedicationDispense cannot be null");
        Long tenantId = TenantContext.getTenantId();

        MedicationDispense entity = mapper.toEntity(dto);

        if (dto.getPatientId() == null) throw new BusinessException("Patient is required");
        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));

        if (dto.getPrescriptionId() == null) throw new BusinessException("Prescription is required");
        if (!prescriptionRepository.existsByIdAndTenantId(dto.getPrescriptionId(), tenantId))
            throw new BusinessException("Prescripción no encontrada con ID: " + dto.getPrescriptionId());
        entity.setPrescription(entityManager.getReference(Prescription.class, dto.getPrescriptionId()));

        if (dto.getEncounterId() != null) {
            if (!encounterRepository.existsByIdAndTenantId(dto.getEncounterId(), tenantId))
                throw new BusinessException("Encuentro no encontrado con ID: " + dto.getEncounterId());
            entity.setEncounter(entityManager.getReference(Encounter.class, dto.getEncounterId()));
        }

        if (dto.getDispenserId() != null) {
            if (!doctorRepository.existsByIdAndTenantId(dto.getDispenserId(), tenantId))
                throw new BusinessException("Dispensador no encontrado con ID: " + dto.getDispenserId());
            entity.setDispenser(entityManager.getReference(Doctor.class, dto.getDispenserId()));
        }

        return repository.save(entity);
    }

    @Override
    public List<MedicationDispense> getByPatient(Long patientId) {
        return repository.findByPatientIdAndTenantId(patientId, TenantContext.getTenantId());
    }

    @Override
    public List<MedicationDispense> getByPrescription(Long prescriptionId) {
        return repository.findByPrescriptionIdAndTenantId(prescriptionId, TenantContext.getTenantId());
    }

    @Override
    protected MedicationDispense mapToEntity(MedicationDispenseDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(MedicationDispenseDto dto, MedicationDispense existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicationDispense entity) { }

    @Override
    protected void publishUpdatedEvent(MedicationDispense entity) { }
}
