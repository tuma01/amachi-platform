package com.amachi.app.vitalia.medicalcatalog.infrastructure.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.MedicalUnitTypeDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.dto.search.MedicalUnitTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.entity.MedicalUnitType;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.mapper.MedicalUnitTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.infrastructure.service.impl.MedicalUnitTypeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mdm/unit-types")
@RequiredArgsConstructor
public class MedicalUnitTypeController extends BaseController implements MedicalUnitTypeApi {

    private final MedicalUnitTypeServiceImpl service;
    private final MedicalUnitTypeMapper mapper;

    @Override
    public ResponseEntity<MedicalUnitTypeDto> getUnitTypeById(@NonNull Long id) {
        MedicalUnitType entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalUnitTypeDto> createUnitType(@Valid @RequestBody @NonNull MedicalUnitTypeDto dto) {
        MedicalUnitType savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalUnitTypeDto> updateUnitType(@NonNull Long id, @Valid @RequestBody @NonNull MedicalUnitTypeDto dto) {
        MedicalUnitType updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteUnitType(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicalUnitTypeDto>> getAllUnitTypes() {
        List<MedicalUnitType> entities = service.getAll();
        List<MedicalUnitTypeDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicalUnitTypeDto>> getPaginatedUnitTypes(
            @NonNull MedicalUnitTypeSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicalUnitType> page = service.getAll(searchDto, pageIndex, pageSize);
        List<MedicalUnitTypeDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<MedicalUnitTypeDto> response = PageResponseDto.<MedicalUnitTypeDto>builder()
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
