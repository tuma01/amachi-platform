package com.amachi.app.vitalia.medicalcore.medicalhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.PastIllnessDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.search.IllnessSearchDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.PastIllness;
import com.amachi.app.vitalia.medicalcore.medicalhistory.mapper.PastIllnessMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.service.PastIllnessService;
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
@RequestMapping("/past-illnesses")
@RequiredArgsConstructor
@Slf4j
public class PastIllnessController extends BaseController implements PastIllnessApi {

    private final PastIllnessService service;
    private final PastIllnessMapper mapper;

    @Override
    public ResponseEntity<PastIllnessDto> getPastIllnessById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<PastIllnessDto> createPastIllness(@Valid @RequestBody @NonNull PastIllnessDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PastIllnessDto> updatePastIllness(@NonNull Long id, @Valid @RequestBody @NonNull PastIllnessDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deletePastIllness(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PastIllnessDto>> getAllPastIllnesses() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<PastIllnessDto>> getPaginatedPastIllnesses(@NonNull IllnessSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<PastIllness> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<PastIllnessDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
