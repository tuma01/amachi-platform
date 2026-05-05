package com.amachi.app.vitalia.medicalcatalog.medication.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.MedicationDto;
import com.amachi.app.vitalia.medicalcatalog.medication.dto.search.MedicationSearchDto;
import com.amachi.app.vitalia.medicalcatalog.medication.entity.Medication;
import com.amachi.app.vitalia.medicalcatalog.medication.mapper.MedicationMapper;
import com.amachi.app.vitalia.medicalcatalog.medication.service.impl.MedicationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mdm/medications")
@RequiredArgsConstructor
public class MedicationController extends BaseController implements MedicationApi {

    private final MedicationServiceImpl service;
    private final MedicationMapper mapper;

    @Override
    public ResponseEntity<MedicationDto> getMedicationById(@NonNull Long id) {
        Medication entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicationDto> createMedication(@Valid @RequestBody @NonNull MedicationDto dto) {
        Medication savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicationDto> updateMedication(@NonNull Long id, @Valid @RequestBody @NonNull MedicationDto dto) {
        Medication savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteMedication(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicationDto>> getAllMedications() {
        List<Medication> entities = service.getAll();
        List<MedicationDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicationDto>> getPaginatedMedications(
            @NonNull MedicationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Medication> page = service.getAll(searchDto, pageIndex, pageSize);
        List<MedicationDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<MedicationDto> response = PageResponseDto.<MedicationDto>builder()
                .content(dtos)
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }
}
