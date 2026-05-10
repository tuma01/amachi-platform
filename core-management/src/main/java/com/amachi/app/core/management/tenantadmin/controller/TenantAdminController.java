package com.amachi.app.core.management.tenantadmin.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.AddressService;
import com.amachi.app.core.management.tenantadmin.dto.TenantAdminDto;
import com.amachi.app.core.management.tenantadmin.dto.search.TenantAdminSearchDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import com.amachi.app.core.management.tenantadmin.mapper.TenantAdminMapper;
import com.amachi.app.core.management.tenantadmin.service.TenantAdminDomainService;
import com.amachi.app.core.management.tenantadmin.service.TenantAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tenant Admin Management", description = "Gestión de administradores de tenants")
@RestController
@RequestMapping("/super-admin/tenant-admins") // Restoring original path for frontend compatibility
@RequiredArgsConstructor
public class TenantAdminController extends BaseController implements TenantAdminApi {

    private final TenantAdminService service;
    private final TenantAdminMapper mapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final TenantAdminDomainService tenantAdminDomainService;

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantAdminDto> getTenantAdminById(Long id) {
        TenantAdmin entity = service.getById(id);
        TenantAdminDto dto = tenantAdminDomainService.enrichTenantAdminDto(entity, mapper.toDto(entity));
        return ResponseEntity.ok(dto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantAdminDto> createTenantAdmin(TenantAdminDto dto) {
        TenantAdmin entity = mapper.toEntity(dto);
        tenantAdminDomainService.handleTenantAddress(entity, dto);
        tenantAdminDomainService.encodePasswordIfNeeded(entity);
        TenantAdmin savedEntity = service.create(entity);
        TenantAdminDto savedDto = mapper.toDto(savedEntity);
        // Enrich DTO to include Tenant Address which is stored by ID
        savedDto = tenantAdminDomainService.enrichTenantAdminDto(savedEntity, savedDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantAdminDto> updateTenantAdmin(Long id, TenantAdminDto dto) {
        TenantAdmin existingEntity = service.getById(id);
        mapper.updateEntityFromDto(dto, existingEntity);
        tenantAdminDomainService.handleTenantAddress(existingEntity, dto);
        tenantAdminDomainService.encodePasswordIfPresent(existingEntity, dto);
        TenantAdmin updatedEntity = service.update(id, existingEntity);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteTenantAdmin(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TenantAdminDto>> getAllTenantAdmins() {
        List<TenantAdmin> entities = service.getAll();
        List<TenantAdminDto> dtos = entities.stream()
                .map(entity -> {
                    TenantAdminDto dto = mapper.toDto(entity);
                    return tenantAdminDomainService.enrichTenantAdminDto(entity, dto);
                }).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PageResponseDto<TenantAdminDto>> getPaginatedTenantAdmins(TenantAdminSearchDto searchDto,
            Integer pageIndex, Integer pageSize) {
        Page<TenantAdmin> page = service.getAll(searchDto, pageIndex, pageSize);
        List<TenantAdminDto> dtos = page.getContent()
                .stream()
                .map(entity -> {
                    TenantAdminDto dto = mapper.toDto(entity);
                    return tenantAdminDomainService.enrichTenantAdminDto(entity, dto);
                })
                .toList();
        PageResponseDto<TenantAdminDto> response = PageResponseDto.<TenantAdminDto>builder()
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
    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> resetAdminPassword(Long id, String newPassword) {
        // 1. Buscamos el admin (el servicio debería lanzar una excepción si no existe)
        TenantAdmin tenantAdmin = service.getById(id);

        // 2. Definimos la contraseña con un valor por defecto seguro
        String passwordToSet = (newPassword != null && !newPassword.isBlank())
                ? newPassword
                : "ChangeMe123!";

        // 3. Ejecutamos la lógica de dominio
        tenantAdminDomainService.resetAdminPassword(tenantAdmin, passwordToSet);

        // 4. Retornamos 200 OK
        return ResponseEntity.ok().build();
    }
}
