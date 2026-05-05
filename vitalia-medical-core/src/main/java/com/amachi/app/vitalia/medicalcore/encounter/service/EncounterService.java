package com.amachi.app.vitalia.medicalcore.encounter.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.ConditionRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.StartEncounterRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.search.EncounterSearchDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Condition;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.request.ObservationRequest;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;

/**
 * Interfaz de servicio clínico de grado hospitalario (FHIR Tier).
 * Orquesta el acto médico y la integridad clínica de los datos.
 */
public interface EncounterService extends GenericService<Encounter, EncounterDto, EncounterSearchDto> {

    /**
     * Inicia un encuentro clínico validando check-in logístico.
     */
    EncounterDto startEncounter(StartEncounterRequest request);
//Juan: solo lo necesario
//    /**
//     * Reanuda un encuentro en pausa (ON_HOLD).
//     */
//    EncounterDto resumeEncounter(Long encounterId);
//
//    /**
//     * Pausa temporalmente un encuentro (ej: espera de ex├ímen).
//     */
//    EncounterDto holdEncounter(Long encounterId, String reason);

    /**
     * Finaliza el encuentro validando diagn├│stico y profesional.
     */
    EncounterDto  completeEncounter(Long encounterId);

    /**
     * Cancela el encuentro si no ha sido finalizado.
     */
    EncounterDto  cancelEncounter(Long encounterId, String reason);

    /**
     * Registro transaccional de diagn├│stico (CIE-10).
     */
    ConditionDto addCondition(Long encounterId, ConditionRequest request);

    /**
     * Registro transaccional de medici├│n cl├¡nica (LOINC).
     */
    ObservationDto addObservation(Long encounterId, ObservationRequest request);

    /**
     * Generation of a medical prescription linked to the clinical encounter.
     */
    Prescription prescribeMedication(Long encounterId, Prescription medicationRequest);
}
