package com.amachi.app.vitalia.medicalcore.hospitalization.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.HospitalizationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.HospitalizationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import com.amachi.app.vitalia.medicalcore.hospitalization.mapper.HospitalizationMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.service.impl.HospitalizationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospitalizations")
@RequiredArgsConstructor
@Slf4j
public class HospitalizationController extends BaseController implements HospitalizationApi {

    private final HospitalizationServiceImpl service;
    private final HospitalizationMapper mapper;

    @Override
    public ResponseEntity<HospitalizationDto> getHospitalizationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<HospitalizationDto> createHospitalization(@Valid @RequestBody @NonNull HospitalizationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HospitalizationDto> updateHospitalization(@NonNull Long id, @Valid @RequestBody @NonNull HospitalizationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteHospitalization(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<HospitalizationDto>> getAllHospitalizations() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<HospitalizationDto>> getPaginatedHospitalizations(@NonNull HospitalizationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Hospitalization> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<HospitalizationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }

    @Override
    public ResponseEntity<List<HospitalizationDto>> getHospitalizationsByPatient(@NonNull Long patientId) {
        return ResponseEntity.ok(service.getByPatientId(patientId).stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<HospitalizationDto> dischargePatient(@NonNull Long id, String dischargeSummary) {
        return ResponseEntity.ok(mapper.toDto(service.dischargePatient(id, dischargeSummary)));
    }
}
