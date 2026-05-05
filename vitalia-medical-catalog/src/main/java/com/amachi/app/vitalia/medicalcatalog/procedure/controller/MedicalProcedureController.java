package com.amachi.app.vitalia.medicalcatalog.procedure.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.MedicalProcedureDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.dto.search.MedicalProcedureSearchDto;
import com.amachi.app.vitalia.medicalcatalog.procedure.entity.MedicalProcedure;
import com.amachi.app.vitalia.medicalcatalog.procedure.mapper.MedicalProcedureMapper;
import com.amachi.app.vitalia.medicalcatalog.procedure.service.impl.MedicalProcedureServiceImpl;
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
@RequestMapping("/mdm/procedures")
@RequiredArgsConstructor
public class MedicalProcedureController extends BaseController implements MedicalProcedureApi {

    private final MedicalProcedureServiceImpl service;
    private final MedicalProcedureMapper mapper;

    @Override
    public ResponseEntity<MedicalProcedureDto> getProcedureById(@NonNull Long id) {
        MedicalProcedure entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalProcedureDto> createProcedure(@Valid @RequestBody @NonNull MedicalProcedureDto dto) {
        MedicalProcedure savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalProcedureDto> updateProcedure(@NonNull Long id, @Valid @RequestBody @NonNull MedicalProcedureDto dto) {
        MedicalProcedure savedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteProcedure(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicalProcedureDto>> getAllProcedures() {
        List<MedicalProcedure> entities = service.getAll();
        List<MedicalProcedureDto> dtos = entities.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicalProcedureDto>> getPaginatedProcedures(
            @NonNull MedicalProcedureSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicalProcedure> page = service.getAll(searchDto, pageIndex, pageSize);
        List<MedicalProcedureDto> dtos = page.getContent().stream().map(mapper::toDto).toList();

        PageResponseDto<MedicalProcedureDto> response = PageResponseDto.<MedicalProcedureDto>builder()
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
