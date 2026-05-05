package com.amachi.app.core.geography.province.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.province.dto.ProvinceDto;
import com.amachi.app.core.geography.province.dto.search.ProvinceSearchDto;
import com.amachi.app.core.geography.province.entity.Province;
import com.amachi.app.core.geography.province.mapper.ProvinceMapper;
import com.amachi.app.core.geography.province.service.ProvinceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Orquestador de Provincias (SaaS Elite Tier).
 * Certificado: DTO-First (Sin manejo manual de entidades).
 */
@RestController
@RequestMapping("/provinces")
@RequiredArgsConstructor
@Slf4j
public class ProvinceController extends BaseController implements ProvinceApi {

    private final ProvinceService service;
    private final ProvinceMapper mapper;

    @Override
    public ResponseEntity<ProvinceDto> getProvinceById(@NonNull @PathVariable Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ProvinceDto> createProvince(@Valid @RequestBody @NonNull ProvinceDto dto) {
        Province entity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<ProvinceDto> updateProvince(@NonNull Long id, @Valid @RequestBody @NonNull ProvinceDto dto) {
        Province entity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<Void> deleteProvince(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ProvinceDto>> getAllProvinces() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ProvinceDto>> getPaginatedProvinces(
            @NonNull ProvinceSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Province> page = service.getAll(searchDto, pageIndex, pageSize);
        
        PageResponseDto<ProvinceDto> response = PageResponseDto.<ProvinceDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
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
