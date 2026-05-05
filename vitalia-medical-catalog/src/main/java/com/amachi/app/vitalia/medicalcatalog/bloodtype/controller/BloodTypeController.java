package com.amachi.app.vitalia.medicalcatalog.bloodtype.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.BloodTypeDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.dto.search.BloodTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.entity.BloodType;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.mapper.BloodTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.bloodtype.service.impl.BloodTypeServiceImpl;
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
@RequestMapping("/mdm/blood-types")
@RequiredArgsConstructor
public class BloodTypeController extends BaseController implements BloodTypeApi {

    private final BloodTypeServiceImpl service;
    private final BloodTypeMapper mapper;

    @Override
    public ResponseEntity<BloodTypeDto> getBloodTypeById(@NonNull Long id) {
        BloodType entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<BloodTypeDto> createBloodType(@Valid @RequestBody @NonNull BloodTypeDto dto) {
        BloodType savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<BloodTypeDto> updateBloodType(@NonNull Long id, @Valid @RequestBody @NonNull BloodTypeDto dto) {
        BloodType updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteBloodType(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<BloodTypeDto>> getAllBloodTypes() {
        List<BloodType> entities = service.getAll();
        List<BloodTypeDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<BloodTypeDto>> getPaginatedBloodTypes(
            @NonNull BloodTypeSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<BloodType> page = service.getAll(searchDto, pageIndex, pageSize);
        List<BloodTypeDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<BloodTypeDto> response = PageResponseDto.<BloodTypeDto>builder()
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
