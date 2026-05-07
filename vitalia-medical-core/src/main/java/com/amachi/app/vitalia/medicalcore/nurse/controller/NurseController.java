package com.amachi.app.vitalia.medicalcore.nurse.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.nurse.dto.NurseDto;
import com.amachi.app.vitalia.medicalcore.nurse.dto.search.NurseSearchDto;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.mapper.NurseMapper;
import com.amachi.app.vitalia.medicalcore.nurse.service.impl.NurseServiceImpl;
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
@RequestMapping("/nurses")
@RequiredArgsConstructor
@Slf4j
public class NurseController extends BaseController implements NurseApi {

    private final NurseServiceImpl service;
    private final NurseMapper mapper;

    @Override
    public ResponseEntity<NurseDto> getNurseById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<NurseDto> createNurse(@Valid @RequestBody @NonNull NurseDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<NurseDto> updateNurse(@NonNull Long id, @Valid @RequestBody @NonNull NurseDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteNurse(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<NurseDto>> getAllNurses() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<NurseDto>> getPaginatedNurses(@NonNull NurseSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Nurse> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<NurseDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
