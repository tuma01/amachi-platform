package com.amachi.app.core.auth.controller;

import com.amachi.app.core.auth.dto.RoleDto;
import com.amachi.app.core.auth.dto.search.RoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.mapper.RoleMapper;
import com.amachi.app.core.auth.service.impl.RoleServiceImpl;
import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController extends BaseController implements RoleApi {

    private final RoleServiceImpl service;
    private final RoleMapper mapper;

    @Override
    public ResponseEntity<RoleDto> getRoleById(@NonNull Long id) {
        Role entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<RoleDto> createRole(@NonNull RoleDto dto) {
        Role savedEntity = service.create(dto);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RoleDto> updateRole(@NonNull Long id, @NonNull RoleDto dto) {
        Role updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteRole(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<Role> entities = service.getAll();
        List<RoleDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<RoleDto>> getPaginatedRoles(
            @NonNull RoleSearchDto roleSearchDto,
            Integer pageIndex, Integer pageSize) {
        Page<Role> page = service.getAll(roleSearchDto, pageIndex, pageSize);
        List<RoleDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<RoleDto> response = PageResponseDto.<RoleDto>builder()
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
