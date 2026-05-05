package com.amachi.app.vitalia.medicalcatalog.diagnosis.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.Icd10Dto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.search.Icd10SearchDto;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.entity.Icd10;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.mapper.Icd10Mapper;
import com.amachi.app.vitalia.medicalcatalog.diagnosis.service.impl.Icd10ServiceImpl;
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
@RequestMapping("/mdm/diagnoses")
@RequiredArgsConstructor
public class Icd10Controller extends BaseController implements Icd10Api {

    private final Icd10ServiceImpl service;
    private final Icd10Mapper mapper;

    @Override
    public ResponseEntity<Icd10Dto> getIcd10ById(@NonNull Long id) {
        Icd10 entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Icd10Dto> createIcd10(@Valid @RequestBody @NonNull Icd10Dto dto) {
        Icd10 savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Icd10Dto> updateIcd10(@NonNull Long id, @Valid @RequestBody @NonNull Icd10Dto dto) {
        Icd10 savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteIcd10(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Icd10Dto>> getAllIcd10() {
        List<Icd10> entities = service.getAll();
        List<Icd10Dto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<Icd10Dto>> getPaginatedIcd10s(
            @NonNull Icd10SearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Icd10> page = service.getAll(searchDto, pageIndex, pageSize);
        List<Icd10Dto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<Icd10Dto> response = PageResponseDto.<Icd10Dto>builder()
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
