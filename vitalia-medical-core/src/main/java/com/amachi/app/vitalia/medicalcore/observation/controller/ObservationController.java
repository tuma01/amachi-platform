package com.amachi.app.vitalia.medicalcore.observation.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.dto.search.ObservationSearchDto;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import com.amachi.app.vitalia.medicalcore.observation.mapper.ObservationMapper;
import com.amachi.app.vitalia.medicalcore.observation.service.ObservationService;
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
@RequestMapping("/observations")
@RequiredArgsConstructor
@Slf4j
public class ObservationController extends BaseController implements ObservationApi {

    private final ObservationService service;
    private final ObservationMapper mapper;

    @Override
    public ResponseEntity<ObservationDto> getObservationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ObservationDto> createObservation(@Valid @RequestBody @NonNull ObservationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ObservationDto> updateObservation(@NonNull Long id, @Valid @RequestBody @NonNull ObservationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteObservation(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ObservationDto>> getAllObservations() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ObservationDto>> getPaginatedObservations(@NonNull ObservationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Observation> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<ObservationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
