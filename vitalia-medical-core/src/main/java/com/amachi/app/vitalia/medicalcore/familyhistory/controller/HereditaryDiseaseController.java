package com.amachi.app.vitalia.medicalcore.familyhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.HereditaryDiseaseDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.dto.search.FamilyHistorySearchDto;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.HereditaryDisease;
import com.amachi.app.vitalia.medicalcore.familyhistory.mapper.HereditaryDiseaseMapper;
import com.amachi.app.vitalia.medicalcore.familyhistory.service.impl.HereditaryDiseaseServiceImpl;
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
@RequestMapping("/hereditary-diseases")
@RequiredArgsConstructor
@Slf4j
public class HereditaryDiseaseController extends BaseController implements HereditaryDiseaseApi {

    private final HereditaryDiseaseServiceImpl service;
    private final HereditaryDiseaseMapper mapper;

    @Override
    public ResponseEntity<HereditaryDiseaseDto> getHereditaryDiseaseById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<HereditaryDiseaseDto> createHereditaryDisease(@Valid @RequestBody @NonNull HereditaryDiseaseDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HereditaryDiseaseDto> updateHereditaryDisease(@NonNull Long id, @Valid @RequestBody @NonNull HereditaryDiseaseDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteHereditaryDisease(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<HereditaryDiseaseDto>> getAllHereditaryDiseases() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<HereditaryDiseaseDto>> getPaginatedHereditaryDiseases(@NonNull FamilyHistorySearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<HereditaryDisease> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<HereditaryDiseaseDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
