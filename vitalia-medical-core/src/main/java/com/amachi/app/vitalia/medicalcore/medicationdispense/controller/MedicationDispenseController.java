package com.amachi.app.vitalia.medicalcore.medicationdispense.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.MedicationDispenseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.search.MedicationDispenseSearchDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import com.amachi.app.vitalia.medicalcore.medicationdispense.mapper.MedicationDispenseMapper;
import com.amachi.app.vitalia.medicalcore.medicationdispense.service.MedicationDispenseService;
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
@RequestMapping("/medication-dispenses")
@RequiredArgsConstructor
@Slf4j
public class MedicationDispenseController extends BaseController implements MedicationDispenseApi {

    private final MedicationDispenseService service;
    private final MedicationDispenseMapper mapper;

    @Override
    public ResponseEntity<MedicationDispenseDto> getMedicationDispenseById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<MedicationDispenseDto> createMedicationDispense(@Valid @RequestBody @NonNull MedicationDispenseDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MedicationDispenseDto> updateMedicationDispense(@NonNull Long id, @Valid @RequestBody @NonNull MedicationDispenseDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteMedicationDispense(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicationDispenseDto>> getAllMedicationDispenses() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicationDispenseDto>> getPaginatedMedicationDispenses(
            @NonNull MedicationDispenseSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicationDispense> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<MedicationDispenseDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }

    @Override
    public ResponseEntity<List<MedicationDispenseDto>> getByPrescription(@NonNull Long prescriptionId) {
        return ResponseEntity.ok(service.getByPrescription(prescriptionId).stream().map(mapper::toDto).toList());
    }
}
