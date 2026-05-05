package com.amachi.app.vitalia.medicalcore.encounter.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;

import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.repository.Icd10Repository;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.appointment.repository.AppointmentRepository;
import com.amachi.app.vitalia.medicalcore.appointment.service.AppointmentService;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterType;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicationRequestStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.ObservationStatus;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.ConditionRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.StartEncounterRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.search.EncounterSearchDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Condition;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.ConditionMapper;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.EncounterMapper;
import com.amachi.app.vitalia.medicalcore.encounter.repository.ConditionRepository;
import com.amachi.app.vitalia.medicalcore.encounter.repository.EncounterRepository;
import com.amachi.app.vitalia.medicalcore.encounter.service.EncounterService;
import com.amachi.app.vitalia.medicalcore.encounter.specification.EncounterSpecification;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.request.ObservationRequest;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import com.amachi.app.vitalia.medicalcore.observation.mapper.ObservationMapper;
import com.amachi.app.vitalia.medicalcore.observation.repository.ObservationRepository;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import com.amachi.app.vitalia.medicalcore.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Enterprise Clinical Orchestrator (SaaS Elite Tier).
 * Manages clinical encounters with FHIR-compliant integrity and multi-tenant isolation.
 */
@Service
@TenantAware
@RequiredArgsConstructor
public class EncounterServiceImpl extends BaseService<Encounter, EncounterDto, EncounterSearchDto> implements EncounterService {

    private final EncounterRepository repository;
    private final ConditionRepository conditionRepository;
    private final ObservationRepository observationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final Icd10Repository icd10Repository;
    private final AppointmentService appointmentService;
    private final DomainEventPublisher eventPublisher;
    private final EncounterMapper mapper;
    private final AppointmentRepository appointmentRepository;
    private final ConditionMapper conditionMapper;
    private final ObservationMapper observationMapper;

    @Override
    protected CommonRepository<Encounter, Long> getRepository() {
        return repository;
    }

    @Override
    protected Specification<Encounter> buildSpecification(EncounterSearchDto searchDto) {
        return new EncounterSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected void publishCreatedEvent(Encounter entity) {
        // Placeholder para publicación de eventos de dominio (SaaS Elite)
    }

    @Override
    protected void publishUpdatedEvent(Encounter entity) {
        // Placeholder para publicación de eventos de dominio (SaaS Elite)
    }

    @Override
    protected Encounter mapToEntity(EncounterDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(EncounterDto dto, Encounter existing) {
        mapper.updateEntityFromDto(dto, existing);
    }

    @Override
    @Transactional
    public EncounterDto startEncounter(StartEncounterRequest request) {
        Appointment appointment = appointmentService.getById(request.getAppointmentId());

        if (appointment.getCheckedInAt() == null) {
            throw new BusinessException("encounter.error.patient.not.checked.in");
        }

        if (appointment.getPatient() == null || appointment.getDoctor() == null) {
            throw new BusinessException("encounter.error.invalid.appointment");
        }
        Long tenantId = TenantContext.getTenantId();
        if (repository.existsByAppointmentIdAndStatusAndTenantId(
                appointment.getId(),
                EncounterStatus.IN_PROGRESS,
                tenantId)) {
            throw new BusinessException("encounter.error.already.in.progress");
        }

        Encounter encounter = Encounter.builder()
                .patient(appointment.getPatient())
                .doctor(appointment.getDoctor())
                .appointment(appointment)
//                .medicalHistory(appointment.getPatient().getMedicalHistory())
                .encounterType(EncounterType.OUTPATIENT)
                .encounterDate(OffsetDateTime.now())
                .status(EncounterStatus.IN_PROGRESS)
                .chiefComplaint(appointment.getReason()) 
                .clinicalNotes(request.getStartNote())
                .tenantId(TenantContext.getTenantId())
                .build();
        return mapper.toDto(repository.save(encounter));
    }

// Juan: solo lo minimo
//    @Override
//    @Transactional
//    public Encounter resumeEncounter(Long encounterId) {
//        Encounter encounter = getById(encounterId);
//        if (encounter.getStatus() != EncounterStatus.ON_HOLD) {
//            throw new BusinessException("encounter.error.not.on.hold");
//        }
//        encounter.setStatus(EncounterStatus.IN_PROGRESS);
//        return update(encounter.getId(), encounter);
//    }
//
//    @Override
//    @Transactional
//    public Encounter holdEncounter(Long encounterId, String reason) {
//        Encounter encounter = getById(encounterId);
//        if (encounter.getStatus() != EncounterStatus.IN_PROGRESS) {
//            throw new BusinessException("encounter.error.must.be.in.progress.to.hold");
//        }
//        encounter.setStatus(EncounterStatus.ON_HOLD);
//        encounter.setNotes(encounter.getNotes() + " | PAUSE: " + reason);
//        return update(encounter.getId(), encounter);
//    }

    @Override
    @Transactional
    public EncounterDto completeEncounter(Long encounterId) {
        Encounter encounter = getById(encounterId);

        encounter.complete();

        // Sincronizar Appointment
        if (encounter.getAppointment() != null) {
            Appointment appt = encounter.getAppointment();
            appt.setStatus(AppointmentStatus.COMPLETED);
            appt.setCompletedAt(OffsetDateTime.now());

            appointmentRepository.save(appt); // ✔ directo
        }

        // Guardar
        return mapper.toDto(repository.save(encounter));
    }

    @Override
    @Transactional
    public EncounterDto cancelEncounter(Long encounterId, String reason) {
        Encounter encounter = getById(encounterId);
        encounter.cancel(reason);
        return mapper.toDto(repository.save(encounter));
    }

    @Override
    @Transactional
    public ConditionDto addCondition(Long encounterId, ConditionRequest request) {
        Encounter encounter = getById(encounterId);
        // 1. Validate encounter status
        if (encounter.getStatus() != EncounterStatus.IN_PROGRESS) {
            throw new BusinessException("encounter.error.must.be.in.progress");
        }

        // 2. Validate doctor (avoid silent NPE)
        if (encounter.getDoctor() == null) {
            throw new BusinessException("encounter.error.doctor.required");
        }

        if (encounter.getPatient() == null) {
            throw new BusinessException("encounter.error.patient.required");
        }

        // 3. Load ICD-10 (tenant-safe if applies)
        Icd10 icd10 = icd10Repository.findById(request.getIcd10Id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Icd10", "error.catalog.not.found", request.getIcd10Id()
                ));

        // 4. (OPTIONAL BUT IMPORTANT) Avoid duplicate diagnosis
        boolean exists = conditionRepository
                .existsByEncounterIdAndIcd10IdAndTenantId(
                        encounter.getId(),
                        icd10.getId(),
                        encounter.getTenantId()
                );

        if (exists) {
            throw new BusinessException("encounter.error.condition.already.exists");
        }

        // 5. Build condition
        Condition condition = Condition.builder()
                .patient(encounter.getPatient())
                .encounter(encounter)
                .doctor(encounter.getDoctor())
//                .medicalHistory(encounter.getMedicalHistory())
                .icd10(icd10)
                .clinicalStatus(request.getClinicalStatus())
                .conditionType(request.getConditionType())
                .severity(request.getSeverity())
                .symptoms(request.getSymptoms())
                .treatmentNotes(request.getTreatmentNotes())
                .diagnosisDate(java.time.LocalDate.now())
                .tenantId(encounter.getTenantId())
                .build();

        // 6. Persist
        Condition saved = conditionRepository.save(condition);

        // 7. Keep in-memory consistency (only if already initialized)
        if (encounter.getConditions() != null) {
            encounter.getConditions().add(saved);
        }
        return conditionMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ObservationDto addObservation(Long encounterId, ObservationRequest request) {
        Encounter encounter = getById(encounterId);
        // 1. Validate encounter status
        if (encounter.getStatus() != EncounterStatus.IN_PROGRESS) {
            throw new BusinessException("encounter.error.must.be.in.progress");
        }

        // 2. Validate doctor
        if (encounter.getDoctor() == null) {
            throw new BusinessException("encounter.error.doctor.required");
        }

        if (encounter.getPatient() == null) {
            throw new BusinessException("encounter.error.patient.required");
        }

        // 3. Build observation
        Observation observation = Observation.builder()
                .patient(encounter.getPatient())
                .encounter(encounter)
                .doctor(encounter.getDoctor())
                .code(request.getCode() != null ? request.getCode().trim().toUpperCase() : null)
                .name(request.getName() != null ? request.getName().trim() : null)
                .value(request.getValue())
                .unit(request.getUnit())
                .interpretation(request.getInterpretation())
                .status(ObservationStatus.FINAL)
                .effectiveDateTime(OffsetDateTime.now())
                .notes(request.getNotes())
                .tenantId(encounter.getTenantId())
                .build();

        return observationMapper.toDto(observationRepository.save(observation));
    }

    @Override
    @Transactional
    public Prescription prescribeMedication(Long encounterId, Prescription prescription) {
        Encounter encounter = getById(encounterId);
        // 1. Validate encounter status
        if (encounter.getStatus() != EncounterStatus.IN_PROGRESS) {
            throw new BusinessException("encounter.error.must.be.in.progress");
        }

        // 2. Validate doctor
        if (encounter.getDoctor() == null) {
            throw new BusinessException("encounter.error.doctor.required");
        }

        // 3. Validate patient
        if (encounter.getPatient() == null) {
            throw new BusinessException("encounter.error.patient.required");
        }

        // 4. Validate medication
        if (prescription.getMedication() == null &&
                (prescription.getMedicationDisplayName() == null || prescription.getMedicationDisplayName().isBlank())) {
            throw new BusinessException("prescription.error.medication.required");
        }
        // 5. Build safe entity (avoid mutating request)
        Prescription prescriptionToSend = Prescription.builder()
                .patient(encounter.getPatient())
                .encounter(encounter)
                .doctor(encounter.getDoctor())
                .medication(prescription.getMedication())
                .medicationDisplayName(prescription.getMedicationDisplayName())
                .dosageInstruction(prescription.getDosageInstruction())
                .route(prescription.getRoute())
                .frequency(prescription.getFrequency())
                .quantity(prescription.getQuantity())
                .priority(prescription.getPriority())
                .reasonCode(prescription.getReasonCode())
                .notes(prescription.getNotes())
                .status(MedicationRequestStatus.ACTIVE)
                .authoredOn(OffsetDateTime.now())
                .tenantId(encounter.getTenantId())
                .build();

        return prescriptionRepository.save(prescriptionToSend);
    }
}
