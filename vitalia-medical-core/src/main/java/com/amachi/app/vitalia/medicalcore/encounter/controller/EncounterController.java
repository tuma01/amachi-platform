package com.amachi.app.vitalia.medicalcore.encounter.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.utils.AppConstants;

import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.ConditionRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.request.StartEncounterRequest;
import com.amachi.app.vitalia.medicalcore.encounter.dto.search.EncounterSearchDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.ConditionMapper;
import com.amachi.app.vitalia.medicalcore.encounter.mapper.EncounterMapper;
import com.amachi.app.vitalia.medicalcore.encounter.service.EncounterService;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.request.ObservationRequest;
import com.amachi.app.vitalia.medicalcore.observation.mapper.ObservationMapper;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(AppConstants.Url.API_V1 + "/encounters")
@RequiredArgsConstructor
public class EncounterController extends BaseController implements EncounterApi {

    private final EncounterService service;
    private final EncounterMapper mapper;
    private final ConditionMapper conditionMapper;
    private final ObservationMapper observationMapper;

    @Override
    public ResponseEntity<EncounterDto> getEncounterById(Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<EncounterDto> createEncounter(EncounterDto dto) {
        Encounter saved = service.create(dto);
        return ResponseEntity.created(
                URI.create(AppConstants.Url.API_V1 + "/encounters/" + saved.getId())
        ).body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<EncounterDto> updateEncounter(Long id, EncounterDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteEncounter(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<EncounterDto>> getAllEncounters() {
        return ResponseEntity.ok(mapper.toDtoList(service.getAll()));
    }

    @Override
    public ResponseEntity<PageResponseDto<EncounterDto>> getPaginatedEncounters(EncounterSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Encounter> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<EncounterDto>builder()
                .content(mapper.toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .build());
    }

    @Override
    public ResponseEntity<EncounterDto> startEncounter(StartEncounterRequest request) {
        return ResponseEntity.ok(service.startEncounter(request));
    }

//    @Override
//    public ResponseEntity<EncounterDto> holdEncounter(Long id, String reason) {
//        return ResponseEntity.ok(mapper.toDto(service.holdEncounter(id, reason)));
//    }
//
//    @Override
//    public ResponseEntity<EncounterDto> resumeEncounter(Long id) {
//        return ResponseEntity.ok(mapper.toDto(service.resumeEncounter(id)));
//    }

    @Override
    public ResponseEntity<EncounterDto> completeEncounter(Long id) {
        return ResponseEntity.ok(service.completeEncounter(id));
    }

    @Override
    public ResponseEntity<EncounterDto> cancelEncounter(Long id, String reason) {
        return ResponseEntity.ok(service.cancelEncounter(id, reason));
    }

    @Override
    public ResponseEntity<ConditionDto> addCondition(Long id, ConditionRequest request) {
        return ResponseEntity.ok(service.addCondition(id, request));
    }

    @Override
    public ResponseEntity<ObservationDto> addObservation(Long id, ObservationRequest request) {
        return ResponseEntity.ok(service.addObservation(id, request));
    }

    @Override
    public ResponseEntity<Void> prescribeMedication(Long id, Prescription request) {
        service.prescribeMedication(id, request);
        return ResponseEntity.noContent().build();
    }
}
