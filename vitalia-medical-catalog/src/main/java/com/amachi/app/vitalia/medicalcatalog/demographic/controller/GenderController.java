package com.amachi.app.vitalia.medicalcatalog.demographic.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.GenderDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.GenderSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.Gender;
import com.amachi.app.vitalia.medicalcatalog.demographic.mapper.GenderMapper;
import com.amachi.app.vitalia.medicalcatalog.demographic.service.impl.GenderServiceImpl;
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
@RequestMapping("/mdm/demographic/genders")
@RequiredArgsConstructor
public class GenderController extends BaseController implements GenderApi {

    private final GenderServiceImpl service;
    private final GenderMapper mapper;

    @Override
    public ResponseEntity<GenderDto> getGenderById(@NonNull Long id) {
        Gender entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<GenderDto> createGender(@Valid @RequestBody @NonNull GenderDto dto) {
        Gender savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<GenderDto> updateGender(@NonNull Long id, @Valid @RequestBody @NonNull GenderDto dto) {
        Gender savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteGender(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<GenderDto>> getAllGenders() {
        List<Gender> entities = service.getAll();
        List<GenderDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<GenderDto>> getPaginatedGenders(
            @NonNull GenderSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Gender> page = service.getAll(searchDto, pageIndex, pageSize);
        List<GenderDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<GenderDto> response = PageResponseDto.<GenderDto>builder()
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
