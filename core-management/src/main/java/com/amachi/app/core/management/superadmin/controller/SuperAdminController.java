package com.amachi.app.core.management.superadmin.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.management.superadmin.dto.SuperAdminDto;
import com.amachi.app.core.management.superadmin.dto.search.SuperAdminSearchDto;
import com.amachi.app.core.management.superadmin.entity.SuperAdmin;
import com.amachi.app.core.management.superadmin.mapper.SuperAdminMapper;
import com.amachi.app.core.management.superadmin.service.SuperAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Tenant Admin Management", description = "Gestión de administradores de tenants")
@RestController
@RequestMapping("/super-admin/") // Restoring original path for frontend compatibility
@RequiredArgsConstructor
public class SuperAdminController extends BaseController implements SuperAdminApi {

    private final SuperAdminService service;
    private final SuperAdminMapper mapper;

    @Override
    public ResponseEntity<SuperAdminDto> getSuperAdminById(Long id) {
        SuperAdmin entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<SuperAdminDto> createSuperAdmin(SuperAdminDto dto) {
        SuperAdmin entity = mapper.toEntity(dto);
        SuperAdmin savedEntity = service.create(entity);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<SuperAdminDto> updateSuperAdmin(Long id, SuperAdminDto dto) {
        SuperAdmin existingEntity = service.getById(id);
        mapper.updateEntityFromDto(dto, existingEntity);
        SuperAdmin updatedEntity = service.update(id, existingEntity);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteSuperAdmin(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<SuperAdminDto>> getAllSuperAdmins() {
        List<SuperAdmin> entities = service.getAll();
        return ResponseEntity.ok(entities.stream().map(mapper::toDto).toList());
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponseDto<SuperAdminDto>> getPaginatedSuperAdmins(SuperAdminSearchDto superAdminSearchDto,
                                                                                  Integer pageIndex, Integer pageSize) {
        Page<SuperAdmin> page = service.getAll(superAdminSearchDto, pageIndex, pageSize);
        List<SuperAdminDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<SuperAdminDto> response = PageResponseDto.<SuperAdminDto>builder()
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
