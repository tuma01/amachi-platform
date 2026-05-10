package com.amachi.app.vitalia.medicalcore.habit.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.ToxicHabitDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.search.HabitSearchDto;
import com.amachi.app.vitalia.medicalcore.habit.entity.ToxicHabit;
import com.amachi.app.vitalia.medicalcore.habit.mapper.ToxicHabitMapper;
import com.amachi.app.vitalia.medicalcore.habit.service.ToxicHabitService;
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
@RequestMapping("/toxic-habits")
@RequiredArgsConstructor
@Slf4j
public class ToxicHabitController extends BaseController implements ToxicHabitApi {

    private final ToxicHabitService service;
    private final ToxicHabitMapper mapper;

    @Override
    public ResponseEntity<ToxicHabitDto> getToxicHabitById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ToxicHabitDto> createToxicHabit(@Valid @RequestBody @NonNull ToxicHabitDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ToxicHabitDto> updateToxicHabit(@NonNull Long id, @Valid @RequestBody @NonNull ToxicHabitDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteToxicHabit(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ToxicHabitDto>> getAllToxicHabits() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ToxicHabitDto>> getPaginatedToxicHabits(@NonNull HabitSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<ToxicHabit> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<ToxicHabitDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
