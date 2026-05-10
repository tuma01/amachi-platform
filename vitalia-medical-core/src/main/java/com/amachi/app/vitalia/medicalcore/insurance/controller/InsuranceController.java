package com.amachi.app.vitalia.medicalcore.insurance.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.InsuranceDto;
import com.amachi.app.vitalia.medicalcore.insurance.dto.search.InsuranceSearchDto;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import com.amachi.app.vitalia.medicalcore.insurance.mapper.InsuranceMapper;
import com.amachi.app.vitalia.medicalcore.insurance.service.InsuranceService;
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
@RequestMapping("/insurances")
@RequiredArgsConstructor
@Slf4j
public class InsuranceController extends BaseController implements InsuranceApi {

    private final InsuranceService service;
    private final InsuranceMapper mapper;

    @Override
    public ResponseEntity<InsuranceDto> getInsuranceById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<InsuranceDto> createInsurance(@Valid @RequestBody @NonNull InsuranceDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<InsuranceDto> updateInsurance(@NonNull Long id, @Valid @RequestBody @NonNull InsuranceDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteInsurance(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<InsuranceDto>> getAllInsurances() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<InsuranceDto>> getPaginatedInsurances(@NonNull InsuranceSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Insurance> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<InsuranceDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
