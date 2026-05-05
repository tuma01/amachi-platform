package com.amachi.app.vitalia.medicalcore.doctor.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.mapper.DoctorMapper;
import com.amachi.app.vitalia.medicalcore.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador homogeneo para el personal medico de Vitalia.
 */
@RestController
@RequestMapping("/personal/doctors")
@RequiredArgsConstructor
public class DoctorController extends BaseController implements DoctorApi {

    private final DoctorService service;
    private final DoctorMapper mapper;

    @Override
    public ResponseEntity<DoctorDto> getDoctorById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<DoctorDto> createDoctor(DoctorDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(service.create(dto)));
    }

    @Override
    public ResponseEntity<DoctorDto> updateDoctor(Long id, DoctorDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteDoctor(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<Doctor> entities = service.getAll();
        List<DoctorDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<DoctorDto>> getPaginatedDoctors(DoctorSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Doctor> page = service.getAll(searchDto, pageIndex, pageSize);
        PageResponseDto<DoctorDto> response = PageResponseDto.<DoctorDto>builder()
                .content(mapper.toDtoList(page.getContent()))
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
