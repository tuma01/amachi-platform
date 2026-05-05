package com.amachi.app.vitalia.medicalcatalog.consultation.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.MedicalConsultationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.dto.search.MedicalConsultationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.consultation.entity.MedicalConsultationType;
import com.amachi.app.vitalia.medicalcatalog.consultation.mapper.MedicalConsultationTypeMapper;
import com.amachi.app.vitalia.medicalcatalog.consultation.service.impl.MedicalConsultationTypeServiceImpl;
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
@RequestMapping("/mdm/consultation-types")
@RequiredArgsConstructor
public class MedicalConsultationTypeController extends BaseController implements MedicalConsultationTypeApi {

    private final MedicalConsultationTypeServiceImpl service;
    private final MedicalConsultationTypeMapper mapper;

    @Override
    public ResponseEntity<MedicalConsultationTypeDto> getConsultationTypeById(@NonNull Long id) {
        MedicalConsultationType entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalConsultationTypeDto> createConsultationType(@Valid @RequestBody @NonNull MedicalConsultationTypeDto dto) {
        MedicalConsultationType savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<MedicalConsultationTypeDto> updateConsultationType(@NonNull Long id, @Valid @RequestBody @NonNull MedicalConsultationTypeDto dto) {
        MedicalConsultationType updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteConsultationType(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MedicalConsultationTypeDto>> getAllConsultationTypes() {
        List<MedicalConsultationType> entities = service.getAll();
        List<MedicalConsultationTypeDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<MedicalConsultationTypeDto>> getPaginatedConsultationTypes(
            @NonNull MedicalConsultationTypeSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<MedicalConsultationType> page = service.getAll(searchDto, pageIndex, pageSize);
        List<MedicalConsultationTypeDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<MedicalConsultationTypeDto> response = PageResponseDto.<MedicalConsultationTypeDto>builder()
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
