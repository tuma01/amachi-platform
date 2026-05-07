package com.amachi.app.vitalia.medicalcore.infrastructure.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.DepartmentUnitDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.DepartmentUnitSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.DepartmentUnitMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.DepartmentUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AppConstants.Url.API_V1 + "/department-units")
@RequiredArgsConstructor
public class DepartmentUnitController extends BaseController implements DepartmentUnitApi {

    private final DepartmentUnitService service;
    private final DepartmentUnitMapper mapper;

    @Override
    public ResponseEntity<DepartmentUnitDto> getDepartmentUnitById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<DepartmentUnitDto> createDepartmentUnit(@NonNull DepartmentUnitDto dto) {
        DepartmentUnit saved = service.create(dto);
        return ResponseEntity.created(
                URI.create(AppConstants.Url.API_V1 + "/department-units/" + saved.getId())
        ).body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<DepartmentUnitDto> updateDepartmentUnit(@NonNull Long id, @NonNull DepartmentUnitDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteDepartmentUnit(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DepartmentUnitDto>> getAllDepartmentUnits() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<DepartmentUnitDto>> getPaginatedDepartmentUnits(
            @NonNull DepartmentUnitSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<DepartmentUnit> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<DepartmentUnitDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build());
    }

    @Override
    public ResponseEntity<List<DepartmentUnitDto>> getRootUnits() {
        return ResponseEntity.ok(service.getRootUnits().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<List<DepartmentUnitDto>> getSubUnits(@NonNull Long parentId) {
        return ResponseEntity.ok(service.getSubUnits(parentId).stream().map(mapper::toDto).toList());
    }
}
