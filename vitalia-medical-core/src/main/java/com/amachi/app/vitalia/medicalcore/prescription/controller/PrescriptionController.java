package com.amachi.app.vitalia.medicalcore.prescription.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.prescription.dto.PrescriptionDto;
import com.amachi.app.vitalia.medicalcore.prescription.dto.search.PrescriptionSearchDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import com.amachi.app.vitalia.medicalcore.prescription.mapper.PrescriptionMapper;
import com.amachi.app.vitalia.medicalcore.prescription.service.impl.PrescriptionServiceImpl;
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
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@Slf4j
public class PrescriptionController extends BaseController implements PrescriptionApi {

    private final PrescriptionServiceImpl service;
    private final PrescriptionMapper mapper;

    @Override
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<PrescriptionDto> createPrescription(@Valid @RequestBody @NonNull PrescriptionDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PrescriptionDto> updatePrescription(@NonNull Long id, @Valid @RequestBody @NonNull PrescriptionDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deletePrescription(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<PrescriptionDto>> getPaginatedPrescriptions(@NonNull PrescriptionSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Prescription> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<PrescriptionDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
