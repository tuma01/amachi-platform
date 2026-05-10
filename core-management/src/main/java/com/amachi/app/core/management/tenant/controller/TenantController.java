package com.amachi.app.core.management.tenant.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.AddressService;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.domain.tenant.dto.search.TenantSearchDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;

import com.amachi.app.core.domain.tenant.mapper.TenantMapper;
import com.amachi.app.core.management.tenant.service.TenantDomainService;
import com.amachi.app.core.management.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TenantController extends BaseController implements TenantApi {

    private final TenantService service;
    private final TenantMapper mapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final TenantDomainService tenantDomainService;

    // --- SUPER ADMIN ENDPOINTS ---
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDto> getTenantById(Long id) {
        Tenant entity = service.getById(id);
        TenantDto dto = mapper.toDto(entity);
        return ResponseEntity.ok(tenantDomainService.enrichTenantDto(entity, dto));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDto> createTenant(TenantDto dto) {
        Tenant entity = mapper.toEntity(dto);
        Tenant savedEntity = service.createWithDetails(entity, dto);
        TenantDto resultDto = mapper.toDto(savedEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tenantDomainService.enrichTenantDto(savedEntity, resultDto));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TenantDto> updateTenant(Long id, TenantDto dto) {
        Tenant existingTenant = service.getById(id);
        mapper.updateEntityFromDto(dto, existingTenant);
        Tenant updatedEntity = service.updateWithDetails(id, existingTenant, dto);
        TenantDto resultDto = mapper.toDto(updatedEntity);
        return ResponseEntity.ok(tenantDomainService.enrichTenantDto(updatedEntity, resultDto));
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteTenant(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TenantDto>> getAllTenants() {
        List<Tenant> entities = service.getAll();
        List<TenantDto> dtos = entities.stream()
                .map(entity -> {
                    TenantDto dto = mapper.toDto(entity);
                    return tenantDomainService.enrichTenantDto(entity, dto);
                }).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<PageResponseDto<TenantDto>> getPaginatedTenants(TenantSearchDto searchDto, Integer pageIndex,
            Integer pageSize) {
        Page<Tenant> page = service.getAll(searchDto, pageIndex, pageSize);
        List<TenantDto> dtos = page.getContent()
                .stream()
                .map(entity -> {
                    TenantDto dto = mapper.toDto(entity);
                    return tenantDomainService.enrichTenantDto(entity, dto);
                })
                .toList();

        PageResponseDto<TenantDto> response = PageResponseDto.<TenantDto>builder()
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

    // --- PUBLIC ENDPOINTS ---
    @Override
    public ResponseEntity<List<TenantDto>> getPublicAllTenants() {
        List<Tenant> entities = service.getAll();
        return ResponseEntity.ok(entities.stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<TenantDto> getPublicTenantByCode(String code) {
        Tenant entity = service.getByCode(code);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    // --- SUPER ADMIN ENDPOINTS EXTRAS ---
    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> enable(Long id) {
        service.enableTenant(id);
        return ResponseEntity.ok().build();
    }

    @Override
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> disable(Long id) {
        service.disableTenant(id);
        return ResponseEntity.ok().build();
    }
}
