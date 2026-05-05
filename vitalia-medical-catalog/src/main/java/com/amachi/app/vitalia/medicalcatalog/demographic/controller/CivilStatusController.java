package com.amachi.app.vitalia.medicalcatalog.demographic.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.CivilStatusDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.dto.search.CivilStatusSearchDto;
import com.amachi.app.vitalia.medicalcatalog.demographic.entity.CivilStatus;
import com.amachi.app.vitalia.medicalcatalog.demographic.mapper.CivilStatusMapper;
import com.amachi.app.vitalia.medicalcatalog.demographic.service.impl.CivilStatusServiceImpl;
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
@RequestMapping("/mdm/demographic/civil-statuses")
@RequiredArgsConstructor
public class CivilStatusController extends BaseController implements CivilStatusApi {

    private final CivilStatusServiceImpl service;
    private final CivilStatusMapper mapper;

    @Override
    public ResponseEntity<CivilStatusDto> getCivilStatusById(@NonNull Long id) {
        CivilStatus entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CivilStatusDto> createCivilStatus(@Valid @RequestBody @NonNull CivilStatusDto dto) {
        CivilStatus savedEntity = service.create(dto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<CivilStatusDto> updateCivilStatus(@NonNull Long id, @Valid @RequestBody @NonNull CivilStatusDto dto) {
        CivilStatus savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteCivilStatus(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<CivilStatusDto>> getAllCivilStatuses() {
        List<CivilStatus> entities = service.getAll();
        List<CivilStatusDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<CivilStatusDto>> getPaginatedCivilStatuses(
            @NonNull CivilStatusSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<CivilStatus> page = service.getAll(searchDto, pageIndex, pageSize);
        List<CivilStatusDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<CivilStatusDto> response = PageResponseDto.<CivilStatusDto>builder()
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
