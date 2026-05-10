package com.amachi.app.vitalia.medicalcore.familyhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.FamilyHistoryDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;
import com.amachi.app.vitalia.medicalcore.familyhistory.mapper.FamilyHistoryMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.service.FamilyHistoryService;
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
@RequestMapping("/family-histories")
@RequiredArgsConstructor
@Slf4j
public class FamilyHistoryController extends BaseController implements FamilyHistoryApi {

    private final FamilyHistoryService service;
    private final FamilyHistoryMapper mapper;

    @Override
    public ResponseEntity<FamilyHistoryDto> getFamilyHistoryById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<FamilyHistoryDto> createFamilyHistory(@Valid @RequestBody @NonNull FamilyHistoryDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<FamilyHistoryDto> updateFamilyHistory(@NonNull Long id, @Valid @RequestBody @NonNull FamilyHistoryDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteFamilyHistory(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<FamilyHistoryDto>> getAllFamilyHistories() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<FamilyHistoryDto>> getPaginatedFamilyHistories(@NonNull FamilyHistorySearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<FamilyHistory> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<FamilyHistoryDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
