package com.amachi.app.vitalia.medicalcore.medicationadministration.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.encounter.repository.EncounterRepository;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.MedicationAdministrationDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search.MedicationAdministrationSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;
import com.amachi.app.vitalia.medicalcore.medicationadministration.mapper.MedicationAdministrationMapper;
import com.amachi.app.vitalia.medicalcore.medicationadministration.repository.MedicationAdministrationRepository;
import com.amachi.app.vitalia.medicalcore.medicationadministration.service.MedicationAdministrationService;
import com.amachi.app.vitalia.medicalcore.medicationadministration.specification.MedicationAdministrationSpecification;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import com.amachi.app.vitalia.medicalcore.medicationdispense.repository.MedicationDispenseRepository;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.repository.NurseRepository;
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
public class MedicationAdministrationServiceImpl
        extends BaseService<MedicationAdministration, MedicationAdministrationDto, MedicationAdministrationSearchDto>
        implements MedicationAdministrationService {

    private final MedicationAdministrationRepository repository;
    private final MedicationAdministrationMapper mapper;
    private final PatientRepository patientRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final MedicationDispenseRepository dispenseRepository;
    private final NurseRepository nurseRepository;
    private final EncounterRepository encounterRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<MedicationAdministration, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<MedicationAdministration> buildSpecification(MedicationAdministrationSearchDto searchDto) {
        return new MedicationAdministrationSpecification(searchDto);
    }

    @Override
    @Transactional
    public MedicationAdministration create(MedicationAdministrationDto dto) {
        if (dto == null) throw new BusinessException("MedicationAdministration cannot be null");
        Long tenantId = TenantContext.getTenantId();

        MedicationAdministration entity = mapper.toEntity(dto);

        if (dto.getPatientId() == null) throw new BusinessException("Patient is required");
        if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
            throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
        entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));

        if (dto.getPrescriptionId() == null) throw new BusinessException("Prescription is required");
        if (!prescriptionRepository.existsByIdAndTenantId(dto.getPrescriptionId(), tenantId))
            throw new BusinessException("Prescripción no encontrada con ID: " + dto.getPrescriptionId());
        entity.setPrescription(entityManager.getReference(Prescription.class, dto.getPrescriptionId()));

        if (dto.getDispenseId() != null) {
            if (!dispenseRepository.existsByIdAndTenantId(dto.getDispenseId(), tenantId))
                throw new BusinessException("Dispensación no encontrada con ID: " + dto.getDispenseId());
            entity.setDispense(entityManager.getReference(MedicationDispense.class, dto.getDispenseId()));
        }

        if (dto.getNurseId() != null) {
            if (!nurseRepository.existsByIdAndTenantId(dto.getNurseId(), tenantId))
                throw new BusinessException("Enfermera no encontrada con ID: " + dto.getNurseId());
            entity.setNurse(entityManager.getReference(Nurse.class, dto.getNurseId()));
        }

        if (dto.getEncounterId() != null) {
            if (!encounterRepository.existsByIdAndTenantId(dto.getEncounterId(), tenantId))
                throw new BusinessException("Encuentro no encontrado con ID: " + dto.getEncounterId());
            entity.setEncounter(entityManager.getReference(Encounter.class, dto.getEncounterId()));
        }

        return repository.save(entity);
    }

    @Override
    public List<MedicationAdministration> getByPatient(Long patientId) {
        return repository.findByPatientIdAndTenantId(patientId, TenantContext.getTenantId());
    }

    @Override
    public List<MedicationAdministration> getByPrescription(Long prescriptionId) {
        return repository.findByPrescriptionIdAndTenantId(prescriptionId, TenantContext.getTenantId());
    }

    @Override
    protected MedicationAdministration mapToEntity(MedicationAdministrationDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(MedicationAdministrationDto dto, MedicationAdministration existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    protected void publishCreatedEvent(MedicationAdministration entity) { }

    @Override
    protected void publishUpdatedEvent(MedicationAdministration entity) { }
}
