package com.amachi.app.core.domain.hospital.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.domain.hospital.dto.HospitalDto;
import com.amachi.app.core.domain.hospital.dto.search.HospitalSearchDto;
import com.amachi.app.core.domain.hospital.entity.Hospital;
import com.amachi.app.core.domain.hospital.mapper.HospitalMapper;
import com.amachi.app.core.domain.hospital.service.impl.HospitalServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
@Slf4j
public class HospitalController extends BaseController implements HospitalApi {

    private final HospitalServiceImpl service;
    private final HospitalMapper mapper;

    @Override
    public ResponseEntity<HospitalDto> getHospitalById(@NonNull Long id) {
        Hospital entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<HospitalDto> createHospital(@Valid @RequestBody @NonNull HospitalDto dto) {
        Hospital entity = mapper.toEntity(dto);
        Hospital savedEntity = service.create(entity);
        return new ResponseEntity<>(mapper.toDto(savedEntity), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HospitalDto> updateHospital(@NonNull Long id, @Valid @RequestBody @NonNull HospitalDto dto) {
        Hospital existingEntity = service.getById(id);
        mapper.updateEntityFromDto(dto, existingEntity);
        Hospital updatedEntity = service.update(id, existingEntity);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteHospital(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<HospitalDto>> getAllHospitals() {
        List<Hospital> entities = service.getAll();
        List<HospitalDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<HospitalDto>> getPaginatedHospitals(@NonNull HospitalSearchDto searchDto,
                                                                              Integer pageIndex, Integer pageSize) {
        Page<Hospital> page = service.getAll(searchDto, pageIndex, pageSize);
        List<HospitalDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<HospitalDto> response = PageResponseDto.<HospitalDto>builder()
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
