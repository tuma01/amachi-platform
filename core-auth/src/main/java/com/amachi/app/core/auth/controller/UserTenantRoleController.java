package com.amachi.app.core.auth.controller;

import com.amachi.app.core.auth.dto.AssignRolesRequest;
import com.amachi.app.core.auth.dto.UserTenantRoleDto;
import com.amachi.app.core.auth.dto.search.UserTenantRoleSearchDto;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.auth.mapper.UserTenantRoleMapper;
import com.amachi.app.core.auth.service.UserTenantRoleService;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-tenant-roles")
@RequiredArgsConstructor
@Slf4j
public class UserTenantRoleController extends BaseController implements UserTenantRoleApi {

    private final UserTenantRoleService service;
    private final UserTenantRoleMapper mapper;

    @Override
    public ResponseEntity<UserTenantRoleDto> getUserTenantRoleById(Long id) {
        UserTenantRole entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<UserTenantRoleDto> createUserTenantRole(UserTenantRoleDto dto) {
        UserTenantRole savedEntity = service.create(dto);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserTenantRoleDto> updateUserTenantRole(Long id, UserTenantRoleDto dto) {
        UserTenantRole updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteUserTenantRole(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<UserTenantRoleDto>> getAllUserTenantRoles() {
        List<UserTenantRole> entities = service.getAll();
        List<UserTenantRoleDto> dtos = entities.stream()
                .map(entity -> mapper.toDto(entity)).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponseDto<UserTenantRoleDto>> getPaginatedUserTenantRoles(
            UserTenantRoleSearchDto userTenantRoleSearchDto,
            Integer pageIndex, Integer pageSize) {
        Page<UserTenantRole> page = service.getAll(userTenantRoleSearchDto, pageIndex, pageSize);
        List<UserTenantRoleDto> dtos = page.getContent()
                .stream()
                .map(userTenantRole -> mapper.toDto(userTenantRole)).toList();

        PageResponseDto<UserTenantRoleDto> response = PageResponseDto.<UserTenantRoleDto>builder()
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

    @Override
    public ResponseEntity<Void> assignRoles(@Valid @RequestBody AssignRolesRequest request) {
        service.assignRolesToUserAndTenant(request.getUserId(), TenantContext.getTenantId(), request.getRoleNames());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> unassignRoles(@Valid @RequestBody AssignRolesRequest request) {
        service.unassignRolesFromUserAndTenant(request.getUserId(), TenantContext.getTenantId(), request.getRoleNames());
        return ResponseEntity.noContent().build();
    }

}
