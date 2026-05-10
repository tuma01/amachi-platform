package com.amachi.app.core.management.tenantconfig.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.management.tenantconfig.dto.TenantConfigDto;
import com.amachi.app.core.management.tenantconfig.dto.search.TenantConfigSearchDto;
import com.amachi.app.core.management.tenantconfig.entity.TenantConfig;
import com.amachi.app.core.management.tenantconfig.mapper.TenantConfigMapper;
import com.amachi.app.core.management.tenantconfig.service.TenantConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/tenantConfigs")
@RequiredArgsConstructor
@Slf4j
public class TenantConfigController extends BaseController implements TenantConfigApi {

    private final TenantConfigService service;
    private final TenantConfigMapper mapper;

    @Override
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantConfigDto> getMyTenantConfig() {
        TenantConfig entity = service.getByCurrentTenant();
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    @GetMapping(value = "/{id:[0-9]+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TenantConfigDto> getTenantConfigById(@PathVariable Long id) {
        TenantConfig entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<TenantConfigDto> createTenantConfig(TenantConfigDto dto) {
        TenantConfig entity = mapper.toEntity(dto);
        TenantConfig savedEntity = service.create(entity);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<TenantConfigDto> updateTenantConfig(Long id, TenantConfigDto dto) {
        TenantConfig existingEntity = service.getById(id);
        mapper.updateEntityFromDto(dto, existingEntity);
        TenantConfig updatedEntity = service.update(id, existingEntity);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteTenantConfig(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<TenantConfigDto>> getAllTenantConfigs() {
        List<TenantConfig> entities = service.getAll();
        List<TenantConfigDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageResponseDto<TenantConfigDto>> getPaginatedTenantConfigs(
            TenantConfigSearchDto tenantConfigSearchDto, Integer pageIndex, Integer pageSize) {
        Page<TenantConfig> page = service.getAll(tenantConfigSearchDto, pageIndex, pageSize);
        List<TenantConfigDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        PageResponseDto<TenantConfigDto> response = PageResponseDto.<TenantConfigDto>builder()
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
