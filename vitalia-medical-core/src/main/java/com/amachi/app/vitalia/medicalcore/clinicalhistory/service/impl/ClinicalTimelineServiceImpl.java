package com.amachi.app.vitalia.medicalcore.clinicalhistory.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalEventDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalSummaryDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.service.ClinicalTimelineService;
import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalEventType;
import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalStatus;
import com.amachi.app.vitalia.medicalcore.common.enums.MedicationRequestStatus;
import com.amachi.app.vitalia.medicalcore.consultation.repository.ConsultationRepository;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.ConditionMapper;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.EncounterMapper;
import com.amachi.app.vitalia.medicalcore.encounter.repository.ConditionRepository;
import com.amachi.app.vitalia.medicalcore.encounter.repository.EncounterRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.repository.HospitalizationRepository;
import com.amachi.app.vitalia.medicalcore.observation.mapper.ObservationMapper;
import com.amachi.app.vitalia.medicalcore.observation.repository.ObservationRepository;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import com.amachi.app.vitalia.medicalcore.prescription.mapper.PrescriptionMapper;
import com.amachi.app.vitalia.medicalcore.prescription.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClinicalTimelineServiceImpl implements ClinicalTimelineService {

    private static final Set<ClinicalStatus> ACTIVE_CONDITION_STATUSES =
            Set.of(ClinicalStatus.ACTIVE, ClinicalStatus.RELAPSE);

    private final EncounterRepository encounterRepository;
    private final ConditionRepository conditionRepository;
    private final ObservationRepository observationRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final HospitalizationRepository hospitalizationRepository;
    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;

    private final EncounterMapper encounterMapper;
    private final ConditionMapper conditionMapper;
    private final ObservationMapper observationMapper;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public List<ClinicalEventDto> getTimeline(Long patientId) {
        Long tenantId = validatePatient(patientId);
        List<ClinicalEventDto> events = new ArrayList<>();

        encounterRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(e ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.ENCOUNTER)
                .eventDate(e.getEncounterDate())
                .summary(e.getEncounterType() + (e.getChiefComplaint() != null ? " — " + e.getChiefComplaint() : ""))
                .referenceId(e.getId())
                .status(e.getStatus().name())
                .build()));

        conditionRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(c ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.CONDITION)
                .eventDate(c.getDiagnosisDate() != null
                        ? c.getDiagnosisDate().atStartOfDay(java.time.ZoneOffset.UTC.normalized()).toOffsetDateTime()
                        : null)
                .summary((c.getName() != null ? c.getName() : "Condition") + " — " + c.getClinicalStatus().name())
                .referenceId(c.getId())
                .code(c.getIcd10() != null ? c.getIcd10().getCode() : null)
                .status(c.getClinicalStatus().name())
                .build()));

        observationRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(o ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.OBSERVATION)
                .eventDate(o.getEffectiveDateTime())
                .summary((o.getName() != null ? o.getName() : o.getCode()) + ": "
                        + o.getValue() + (o.getUnit() != null ? " " + o.getUnit() : ""))
                .referenceId(o.getId())
                .code(o.getCode())
                .status(o.getStatus().name())
                .build()));

        prescriptionRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(p ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.PRESCRIPTION)
                .eventDate(p.getAuthoredOn())
                .summary((p.getMedicationDisplayName() != null ? p.getMedicationDisplayName() : "Medication")
                        + " — " + p.getStatus().name())
                .referenceId(p.getId())
                .status(p.getStatus().name())
                .build()));

        hospitalizationRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(h ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.HOSPITALIZATION)
                .eventDate(h.getAdmissionDate())
                .summary("Hospitalization — " + h.getStatus().name())
                .referenceId(h.getId())
                .status(h.getStatus().name())
                .build()));

        consultationRepository.findByPatientIdAndTenantId(patientId, tenantId).forEach(c ->
            events.add(ClinicalEventDto.builder()
                .type(ClinicalEventType.CONSULTATION)
                .eventDate(c.getConsultationDate())
                .summary("Consultation" + (c.getStatus() != null ? " — " + c.getStatus().name() : ""))
                .referenceId(c.getId())
                .status(c.getStatus() != null ? c.getStatus().name() : null)
                .build()));

        events.sort(Comparator.comparing(ClinicalEventDto::getEventDate,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return events;
    }

    @Override
    public List<ClinicalEventDto> getTimeline(Long patientId, ClinicalEventType type) {
        return getTimeline(patientId).stream()
                .filter(e -> e.getType() == type)
                .toList();
    }

    @Override
    public ClinicalSummaryDto getSummary(Long patientId) {
        Long tenantId = validatePatient(patientId);

        var patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "error.resource.not.found", patientId));

        String fullName = patient.getPerson() != null
                ? patient.getPerson().getFirstName() + " " + (patient.getPerson().getLastName() != null ? patient.getPerson().getLastName() : "")
                : null;

        var encounters     = encounterRepository.findByPatientIdAndTenantId(patientId, tenantId);
        var conditions     = conditionRepository.findByPatientIdAndTenantId(patientId, tenantId);
        var observations   = observationRepository.findByPatientIdAndTenantId(patientId, tenantId);
        var prescriptions  = prescriptionRepository.findByPatientIdAndTenantId(patientId, tenantId);
        var hospitalizations = hospitalizationRepository.findByPatientIdAndTenantId(patientId, tenantId);

        var activeConditions = conditions.stream()
                .filter(c -> ACTIVE_CONDITION_STATUSES.contains(c.getClinicalStatus()))
                .toList();

        var activePrescriptions = prescriptions.stream()
                .filter(p -> p.getStatus() == MedicationRequestStatus.ACTIVE)
                .toList();

        var latestEncounter = encounters.stream()
                .max(Comparator.comparing(e -> e.getEncounterDate() != null ? e.getEncounterDate() : java.time.OffsetDateTime.MIN))
                .map(encounterMapper::toDto)
                .orElse(null);

        var latestObservation = observations.stream()
                .max(Comparator.comparing(o -> o.getEffectiveDateTime() != null ? o.getEffectiveDateTime() : java.time.OffsetDateTime.MIN))
                .map(observationMapper::toDto)
                .orElse(null);

        List<ConditionDto> activeConditionDtos = activeConditions.stream()
                .map(conditionMapper::toDto)
                .toList();

        var activePrescriptionDtos = activePrescriptions.stream()
                .map(prescriptionMapper::toDto)
                .toList();

        return ClinicalSummaryDto.builder()
                .patientId(patientId)
                .patientFullName(fullName)
                .totalEncounters(encounters.size())
                .activeConditions(activeConditions.size())
                .activePrescriptions(activePrescriptions.size())
                .totalHospitalizations(hospitalizations.size())
                .latestEncounter(latestEncounter)
                .latestObservation(latestObservation)
                .activeConditionList(activeConditionDtos)
                .activePrescriptionList(activePrescriptionDtos)
                .build();
    }

    private Long validatePatient(Long patientId) {
        Long tenantId = TenantContext.getTenantId();
        if (!patientRepository.existsByIdAndTenantId(patientId, tenantId))
            throw new ResourceNotFoundException("Patient", "error.resource.not.found", patientId);
        return tenantId;
    }
}
