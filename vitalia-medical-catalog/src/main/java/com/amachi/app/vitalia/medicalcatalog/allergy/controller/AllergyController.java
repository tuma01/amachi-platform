package com.amachi.app.vitalia.medicalcatalog.allergy.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.AllergyDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.dto.search.AllergySearchDto;
import com.amachi.app.vitalia.medicalcatalog.allergy.entity.Allergy;
import com.amachi.app.vitalia.medicalcatalog.allergy.mapper.AllergyMapper;
import com.amachi.app.vitalia.medicalcatalog.allergy.service.impl.AllergyServiceImpl;
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
@RequestMapping("/mdm/allergies")
@RequiredArgsConstructor
public class AllergyController extends BaseController implements AllergyApi {

    private final AllergyServiceImpl service;
    private final AllergyMapper mapper;

    @Override
    public ResponseEntity<AllergyDto> getAllergyById(@NonNull Long id) {
        Allergy entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AllergyDto> createAllergy(@Valid @RequestBody @NonNull AllergyDto dto) {
        Allergy savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<AllergyDto> updateAllergy(@NonNull Long id, @Valid @RequestBody @NonNull AllergyDto dto) {
        Allergy savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAllergy(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AllergyDto>> getAllAllergies() {
        List<Allergy> entities = service.getAll();
        List<AllergyDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<AllergyDto>> getPaginatedAllergies(
            @NonNull AllergySearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Allergy> page = service.getAll(searchDto, pageIndex, pageSize);
        List<AllergyDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<AllergyDto> response = PageResponseDto.<AllergyDto>builder()
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
