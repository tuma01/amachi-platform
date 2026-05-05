package com.amachi.app.vitalia.medicalcatalog.identity.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.IdentificationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.search.IdentificationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.identity.entity.IdentificationType;
import com.amachi.app.vitalia.medicalcatalog.identity.mapper.IdentificationTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.identity.service.impl.IdentificationTypeServiceImpl;
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
@RequestMapping("/mdm/identity/types")
@RequiredArgsConstructor
public class IdentificationTypeController extends BaseController implements IdentificationTypeApi {

    private final IdentificationTypeServiceImpl service;
    private final IdentificationTypeMapper mapper;

    @Override
    public ResponseEntity<IdentificationTypeDto> getIdentificationTypeById(@NonNull Long id) {
        IdentificationType entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<IdentificationTypeDto> createIdentificationType(@Valid @RequestBody @NonNull IdentificationTypeDto dto) {
        IdentificationType savedEntity = service.create(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<IdentificationTypeDto> updateIdentificationType(@NonNull Long id, @Valid @RequestBody @NonNull IdentificationTypeDto dto) {
        IdentificationType savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteIdentificationType(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<IdentificationTypeDto>> getAllIdentificationTypes() {
        List<IdentificationType> entities = service.getAll();
        List<IdentificationTypeDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<IdentificationTypeDto>> getPaginatedIdentificationTypes(
            @NonNull IdentificationTypeSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<IdentificationType> page = service.getAll(searchDto, pageIndex, pageSize);
        List<IdentificationTypeDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<IdentificationTypeDto> response = PageResponseDto.<IdentificationTypeDto>builder()
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
