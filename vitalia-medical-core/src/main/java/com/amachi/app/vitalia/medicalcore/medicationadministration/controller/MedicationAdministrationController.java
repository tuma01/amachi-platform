package com.amachi.app.vitalia.medicalcore.medicationadministration.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.MedicationAdministrationDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.search.MedicationAdministrationSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;
import com.amachi.app.vitalia.medicalcore.medicationadministration.mapper.MedicationAdministrationMapper;
import com.amachi.app.vitalia.medicalcore.medicationadministration.service.MedicationAdministrationService;
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
@RequestMapping("/medication-administrations")
@RequiredArgsConstructor
@Slf4j
public class MedicationAdministrationController extends BaseController implements MedicationAdministrationApi {

    private final MedicationAdministrationService service;
    private final MedicationAdministrationMapper mapper;

    @Override
    public ResponseEntity<MedicationAdministrationDto> getMedicationAdministrationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<MedicationAdministrationDto> createMedicationAdministration(@Valid @RequestBody @NonNull MedicationAdministrationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MedicationAdministrationDto> updateMedicationAdministration(@NonNull Long id, @Valid @RequestBody @NonNull MedicationAdministrationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteMedicationAdministration(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicationAdministrationDto>> getAllMedicationAdministrations() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicationAdministrationDto>> getPaginatedMedicationAdministrations(
            @NonNull MedicationAdministrationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicationAdministration> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<MedicationAdministrationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }

    @Override
    public ResponseEntity<List<MedicationAdministrationDto>> getByPrescription(@NonNull Long prescriptionId) {
        return ResponseEntity.ok(service.getByPrescription(prescriptionId).stream().map(mapper::toDto).toList());
    }
}
