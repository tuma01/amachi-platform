package com.amachi.app.vitalia.medicalcore.medicalhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.ActualIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.ActualIllness;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.ActualIllnessMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.impl.ActualIllnessServiceImpl;
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
@RequestMapping("/actual-illnesses")
@RequiredArgsConstructor
@Slf4j
public class ActualIllnessController extends BaseController implements ActualIllnessApi {

    private final ActualIllnessServiceImpl service;
    private final ActualIllnessMapper mapper;

    @Override
    public ResponseEntity<ActualIllnessDto> getActualIllnessById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ActualIllnessDto> createActualIllness(@Valid @RequestBody @NonNull ActualIllnessDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ActualIllnessDto> updateActualIllness(@NonNull Long id, @Valid @RequestBody @NonNull ActualIllnessDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteActualIllness(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ActualIllnessDto>> getAllActualIllnesses() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ActualIllnessDto>> getPaginatedActualIllnesses(@NonNull IllnessSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<ActualIllness> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<ActualIllnessDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
