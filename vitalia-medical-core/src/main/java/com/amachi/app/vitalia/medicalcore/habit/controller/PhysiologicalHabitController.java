package com.amachi.app.vitalia.medicalcore.habit.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.PhysiologicalHabitDto;
import com.amachi.app.vitalia.medicalcore.habit.dto.search.HabitSearchDto;
import com.amachi.app.vitalia.medicalcore.habit.entity.PhysiologicalHabit;
import com.amachi.app.vitalia.medicalcore.habit.mapper.PhysiologicalHabitMapper;
import com.amachi.app.vitalia.medicalcore.habit.service.impl.PhysiologicalHabitServiceImpl;
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
@RequestMapping("/physiological-habits")
@RequiredArgsConstructor
@Slf4j
public class PhysiologicalHabitController extends BaseController implements PhysiologicalHabitApi {

    private final PhysiologicalHabitServiceImpl service;
    private final PhysiologicalHabitMapper mapper;

    @Override
    public ResponseEntity<PhysiologicalHabitDto> getPhysiologicalHabitById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<PhysiologicalHabitDto> createPhysiologicalHabit(@Valid @RequestBody @NonNull PhysiologicalHabitDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PhysiologicalHabitDto> updatePhysiologicalHabit(@NonNull Long id, @Valid @RequestBody @NonNull PhysiologicalHabitDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deletePhysiologicalHabit(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PhysiologicalHabitDto>> getAllPhysiologicalHabits() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<PhysiologicalHabitDto>> getPaginatedPhysiologicalHabits(@NonNull HabitSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<PhysiologicalHabit> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<PhysiologicalHabitDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
