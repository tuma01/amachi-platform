package com.amachi.app.vitalia.medicalcore.medicalhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.MedicalHistoryDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.MedicalHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.MedicalHistoryMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.MedicalHistoryService;
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
@RequestMapping("/medical-histories")
@RequiredArgsConstructor
@Slf4j
public class MedicalHistoryController extends BaseController implements MedicalHistoryApi {

    private final MedicalHistoryService service;
    private final MedicalHistoryMapper mapper;

    @Override
    public ResponseEntity<MedicalHistoryDto> getMedicalHistoryById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<MedicalHistoryDto> createMedicalHistory(@Valid @RequestBody @NonNull MedicalHistoryDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<MedicalHistoryDto> updateMedicalHistory(@NonNull Long id, @Valid @RequestBody @NonNull MedicalHistoryDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteMedicalHistory(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicalHistoryDto>> getAllMedicalHistories() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicalHistoryDto>> getPaginatedMedicalHistories(@NonNull MedicalHistorySearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicalHistory> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<MedicalHistoryDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
