package com.amachi.app.vitalia.medicalcore.hospitalization.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationStatus;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.encounter.repository.EncounterRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.HospitalizationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.HospitalizationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import com.amachi.app.vitalia.medicalcore.hospitalization.mapper.HospitalizationMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.repository.HospitalizationRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.service.HospitalizationService;
import com.amachi.app.vitalia.medicalcore.hospitalization.specification.HospitalizationSpecification;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.BedRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.RoomRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.BedService;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import com.amachi.app.vitalia.medicalcore.insurance.repository.InsuranceRepository;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.repository.NurseRepository;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalizationServiceImpl
        extends BaseService<Hospitalization, HospitalizationDto, HospitalizationSearchDto>
        implements HospitalizationService {

    private final HospitalizationRepository repository;
    private final HospitalizationMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;
    private final EncounterRepository encounterRepository;
    private final DepartmentUnitRepository departmentUnitRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;
    private final BedService bedService;
    private final InsuranceRepository insuranceRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Hospitalization, Long> getRepository() { return repository; }

    @Override
    protected Specification<Hospitalization> buildSpecification(HospitalizationSearchDto s) {
        return new HospitalizationSpecification(s);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Hospitalization entity) {}

    @Override
    protected void publishUpdatedEvent(Hospitalization entity) {}

    @Override
    protected Hospitalization mapToEntity(HospitalizationDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(HospitalizationDto dto, Hospitalization existing) {
        Long tenantId = TenantContext.getTenantId();
        mapper.updateEntityFromDto(dto, existing);
        resolveRelations(dto, existing, tenantId);
    }

    @Override
    @Transactional
    public Hospitalization create(HospitalizationDto dto) {
        if (dto == null) throw new BusinessException("Hospitalization data cannot be null");

        Long tenantId = TenantContext.getTenantId();
        Hospitalization entity = mapper.toEntity(dto);
        resolveRelations(dto, entity, tenantId);

        if (entity.getBed() != null) {
            bedService.updateStatus(entity.getBed().getId(), BedStatusEnum.OCCUPIED);
        }

        return repository.save(entity);
    }

    @Override
    @Transactional
    public Hospitalization update(Long id, HospitalizationDto dto) {
        Hospitalization existing = getById(id);
        Long oldBedId = existing.getBed() != null ? existing.getBed().getId() : null;

        mergeEntities(dto, existing);

        Long newBedId = existing.getBed() != null ? existing.getBed().getId() : null;

        if (oldBedId != null && !oldBedId.equals(newBedId)) {
            bedService.updateStatus(oldBedId, BedStatusEnum.AVAILABLE);
        }
        if (newBedId != null && !newBedId.equals(oldBedId)) {
            bedService.updateStatus(newBedId, BedStatusEnum.OCCUPIED);
        }

        return repository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Hospitalization hosp = getById(id);
        if (hosp.getBed() != null) {
            bedService.updateStatus(hosp.getBed().getId(), BedStatusEnum.AVAILABLE);
        }
        super.delete(id);
    }

    @Override
    @Transactional
    public Hospitalization dischargePatient(Long id, String dischargeSummary) {
        Hospitalization hosp = getById(id);
        hosp.setDischargeDate(OffsetDateTime.now());
        hosp.setStatus(HospitalizationStatus.DISCHARGED);
        hosp.setDischargeReason(dischargeSummary);

        if (hosp.getBed() != null) {
            bedService.updateStatus(hosp.getBed().getId(), BedStatusEnum.AVAILABLE);
        }

        return repository.save(hosp);
    }

    @Override
    public List<Hospitalization> getByPatientId(Long patientId) {
        return repository.findByPatientIdAndTenantId(patientId, TenantContext.getTenantId());
    }

    private void resolveRelations(HospitalizationDto dto, Hospitalization entity, Long tenantId) {
        if (dto.getPatientId() != null) {
            if (!patientRepository.existsByIdAndTenantId(dto.getPatientId(), tenantId))
                throw new BusinessException("Paciente no encontrado con ID: " + dto.getPatientId());
            entity.setPatient(entityManager.getReference(Patient.class, dto.getPatientId()));
        }
        if (dto.getDoctorId() != null) {
            if (!doctorRepository.existsByIdAndTenantId(dto.getDoctorId(), tenantId))
                throw new BusinessException("Médico no encontrado con ID: " + dto.getDoctorId());
            entity.setDoctor(entityManager.getReference(Doctor.class, dto.getDoctorId()));
        }
        if (dto.getNurseId() != null) {
            if (!nurseRepository.existsByIdAndTenantId(dto.getNurseId(), tenantId))
                throw new BusinessException("Enfermera no encontrada con ID: " + dto.getNurseId());
            entity.setNurse(entityManager.getReference(Nurse.class, dto.getNurseId()));
        } else {
            entity.setNurse(null);
        }
        if (dto.getEncounterId() != null) {
            if (!encounterRepository.existsByIdAndTenantId(dto.getEncounterId(), tenantId))
                throw new BusinessException("Encounter no encontrado con ID: " + dto.getEncounterId());
            entity.setEncounter(entityManager.getReference(Encounter.class, dto.getEncounterId()));
        } else {
            entity.setEncounter(null);
        }
        if (dto.getUnitId() != null) {
            if (!departmentUnitRepository.existsByIdAndTenantId(dto.getUnitId(), tenantId))
                throw new BusinessException("Unidad no encontrada con ID: " + dto.getUnitId());
            entity.setUnit(entityManager.getReference(DepartmentUnit.class, dto.getUnitId()));
        } else {
            entity.setUnit(null);
        }
        if (dto.getRoomId() != null) {
            if (!roomRepository.existsByIdAndTenantId(dto.getRoomId(), tenantId))
                throw new BusinessException("Habitación no encontrada con ID: " + dto.getRoomId());
            entity.setRoom(entityManager.getReference(Room.class, dto.getRoomId()));
        } else {
            entity.setRoom(null);
        }
        if (dto.getBedId() != null) {
            if (!bedRepository.existsByIdAndTenantId(dto.getBedId(), tenantId))
                throw new BusinessException("Cama no encontrada con ID: " + dto.getBedId());
            entity.setBed(entityManager.getReference(Bed.class, dto.getBedId()));
        } else {
            entity.setBed(null);
        }
        if (dto.getInsuranceId() != null) {
            if (!insuranceRepository.existsByIdAndTenantId(dto.getInsuranceId(), tenantId))
                throw new BusinessException("Seguro no encontrado con ID: " + dto.getInsuranceId());
            entity.setInsurance(entityManager.getReference(Insurance.class, dto.getInsuranceId()));
        } else {
            entity.setInsurance(null);
        }
    }
}
