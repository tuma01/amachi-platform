package com.amachi.app.vitalia.medicalcore.doctor.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorHospitalAssignmentDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorHospitalAssignmentSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.DoctorHospitalAssignment;
import com.amachi.app.vitalia.medicalcore.doctor.mapper.DoctorHospitalAssignmentMapper;
import com.amachi.app.vitalia.medicalcore.doctor.service.impl.DoctorHospitalAssignmentServiceImpl;
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
@RequestMapping("/doctor-hospital-assignments")
@RequiredArgsConstructor
@Slf4j
public class DoctorHospitalAssignmentController extends BaseController implements DoctorHospitalAssignmentApi {

    private final DoctorHospitalAssignmentServiceImpl service;
    private final DoctorHospitalAssignmentMapper mapper;

    @Override
    public ResponseEntity<DoctorHospitalAssignmentDto> getDoctorHospitalAssignmentById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<DoctorHospitalAssignmentDto> createDoctorHospitalAssignment(@Valid @RequestBody @NonNull DoctorHospitalAssignmentDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DoctorHospitalAssignmentDto> updateDoctorHospitalAssignment(@NonNull Long id, @Valid @RequestBody @NonNull DoctorHospitalAssignmentDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteDoctorHospitalAssignment(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DoctorHospitalAssignmentDto>> getAllDoctorHospitalAssignments() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<DoctorHospitalAssignmentDto>> getPaginatedDoctorHospitalAssignments(
            @NonNull DoctorHospitalAssignmentSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<DoctorHospitalAssignment> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<DoctorHospitalAssignmentDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
