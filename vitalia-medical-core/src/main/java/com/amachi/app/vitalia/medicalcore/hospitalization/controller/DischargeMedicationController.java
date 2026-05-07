package com.amachi.app.vitalia.medicalcore.hospitalization.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.DischargeMedicationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.search.DischargeMedicationSearchDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;
import com.amachi.app.vitalia.medicalcore.hospitalization.mapper.DischargeMedicationMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.service.impl.DischargeMedicationServiceImpl;
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
@RequestMapping("/discharge-medications")
@RequiredArgsConstructor
@Slf4j
public class DischargeMedicationController extends BaseController implements DischargeMedicationApi {

    private final DischargeMedicationServiceImpl service;
    private final DischargeMedicationMapper mapper;

    @Override
    public ResponseEntity<DischargeMedicationDto> getDischargeMedicationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<DischargeMedicationDto> createDischargeMedication(@Valid @RequestBody @NonNull DischargeMedicationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DischargeMedicationDto> updateDischargeMedication(@NonNull Long id, @Valid @RequestBody @NonNull DischargeMedicationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteDischargeMedication(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<PageResponseDto<DischargeMedicationDto>> getPaginatedDischargeMedications(@NonNull DischargeMedicationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<DischargeMedication> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<DischargeMedicationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }

    @Override
    public ResponseEntity<List<DischargeMedicationDto>> getByHospitalization(@NonNull Long hospitalizationId) {
        return ResponseEntity.ok(service.getByHospitalizationId(hospitalizationId).stream().map(mapper::toDto).toList());
    }
}
