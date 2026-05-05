package com.amachi.app.core.geography.state.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.geography.state.dto.StateDto;
import com.amachi.app.core.geography.state.dto.search.StateSearchDto;
import com.amachi.app.core.geography.state.entity.State;
import com.amachi.app.core.geography.state.mapper.StateMapper;
import com.amachi.app.core.geography.state.service.impl.StateServiceImpl;
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
@RequestMapping("/states")
@RequiredArgsConstructor
@Slf4j
public class StateController extends BaseController implements StateApi {

    private final StateServiceImpl service;
    private final StateMapper mapper;

    @Override
    public ResponseEntity<StateDto> getStateById(@NonNull @PathVariable Long id) {
        State entity = service.getById(id);
        return ResponseEntity.ok(mapper.toDto(entity));
    }

    @Override
    public ResponseEntity<StateDto> createState(@Valid @RequestBody @NonNull StateDto dto) {
        State savedEntity = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(savedEntity));
    }

    @Override
    public ResponseEntity<StateDto> updateState(@NonNull Long id, @Valid @RequestBody @NonNull StateDto dto) {
        State updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }

    @Override
    public ResponseEntity<Void> deleteState(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<StateDto>> getAllStates() {
        List<State> entities = service.getAll();
        List<StateDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<StateDto>> getPaginatedStates(
            @NonNull StateSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<State> page = service.getAll(searchDto, pageIndex, pageSize);
        List<StateDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();

        PageResponseDto<StateDto> response = PageResponseDto.<StateDto>builder()
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
