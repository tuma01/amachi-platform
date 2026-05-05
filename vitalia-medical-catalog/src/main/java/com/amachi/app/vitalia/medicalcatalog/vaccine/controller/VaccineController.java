package com.amachi.app.vitalia.medicalcatalog.vaccine.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.VaccineDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.dto.search.VaccineSearchDto;
import com.amachi.app.vitalia.medicalcatalog.vaccine.entity.Vaccine;
import com.amachi.app.vitalia.medicalcatalog.vaccine.mapper.VaccineMapper;
import com.amachi.app.vitalia.medicalcatalog.vaccine.service.impl.VaccineServiceImpl;
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
@RequestMapping("/mdm/vaccines")
@RequiredArgsConstructor
public class VaccineController extends BaseController implements VaccineApi {

    private final VaccineServiceImpl service;
    private final VaccineMapper mapper;

    @Override
    public ResponseEntity<VaccineDto> getVaccineById(@NonNull Long id) {
        Vaccine entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<VaccineDto> createVaccine(@Valid @RequestBody @NonNull VaccineDto dto) {
        Vaccine savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<VaccineDto> updateVaccine(@NonNull Long id, @Valid @RequestBody @NonNull VaccineDto dto) {
        Vaccine savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteVaccine(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<VaccineDto>> getAllVaccines() {
        List<Vaccine> entities = service.getAll();
        List<VaccineDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<VaccineDto>> getPaginatedVaccines(
            @NonNull VaccineSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Vaccine> page = service.getAll(searchDto, pageIndex, pageSize);
        List<VaccineDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<VaccineDto> response = PageResponseDto.<VaccineDto>builder()
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
