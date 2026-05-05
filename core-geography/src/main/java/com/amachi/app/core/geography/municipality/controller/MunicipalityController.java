package com.amachi.app.core.geography.municipality.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.municipality.dto.MunicipalityDto;
import com.amachi.app.core.geography.municipality.dto.search.MunicipalitySearchDto;
import com.amachi.app.core.geography.municipality.entity.Municipality;
import com.amachi.app.core.geography.municipality.mapper.MunicipalityMapper;
import com.amachi.app.core.geography.municipality.service.impl.MunicipalityServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municipalities")
@RequiredArgsConstructor
@Slf4j
public class MunicipalityController extends BaseController implements MunicipalityApi {

    private final MunicipalityServiceImpl service;
    private final MunicipalityMapper mapper;

    @Override
    public ResponseEntity<MunicipalityDto> getMunicipalityById(@NonNull Long id) {
        Municipality entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<MunicipalityDto> createMunicipality(@Valid @RequestBody @NonNull MunicipalityDto dto) {
        Municipality savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    public ResponseEntity<MunicipalityDto> updateMunicipality(@NonNull Long id, @Valid @RequestBody @NonNull MunicipalityDto dto) {
        Municipality updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteMunicipality(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<MunicipalityDto>> getAllMunicipalities() {
        List<Municipality> entities = service.getAll();
        List<MunicipalityDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<MunicipalityDto>> getPaginatedMunicipalities(
            @NonNull MunicipalitySearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Municipality> page = service.getAll(searchDto, pageIndex, pageSize);
        List<MunicipalityDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<MunicipalityDto> response = PageResponseDto.<MunicipalityDto>builder()
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
