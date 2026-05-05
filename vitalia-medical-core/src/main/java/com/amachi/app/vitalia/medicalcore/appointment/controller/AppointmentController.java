package com.amachi.app.vitalia.medicalcore.appointment.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.appointment.mapper.AppointmentMapper;
import com.amachi.app.vitalia.medicalcore.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(AppConstants.Url.API_V1 + "/appointments")
@RequiredArgsConstructor
public class AppointmentController extends BaseController implements AppointmentApi {

    private final AppointmentService service;
    private final AppointmentMapper mapper;

    @Override
    public ResponseEntity<AppointmentDto> getAppointmentById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<AppointmentDto> createAppointment(@NonNull AppointmentDto dto) {
        Appointment saved = service.create(dto);
        return ResponseEntity.created(
                URI.create(AppConstants.Url.API_V1 + "/appointments/" + saved.getId())
        ).body(mapper.toDto(saved));
    }

    @Override
    public ResponseEntity<AppointmentDto> updateAppointment(@NonNull Long id, @NonNull AppointmentDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteAppointment(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<Appointment> entities = service.getAll();
        List<AppointmentDto> dtos = entities.stream()
                .map(mapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<PageResponseDto<AppointmentDto>> getPaginatedAppointments(
            @NonNull AppointmentSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Appointment> page = service.getAll(searchDto, pageIndex, pageSize);
        List<AppointmentDto> dtos = page.getContent()
                .stream()
                .map(mapper::toDto).toList();

        PageResponseDto<AppointmentDto> response = PageResponseDto.<AppointmentDto>builder()
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

    @Override
    public ResponseEntity<AppointmentDto> updateAppointmentStatus(Long id, AppointmentStatus status) {
        return ResponseEntity.ok(mapper.toDto(service.updateStatus(id, status)));
    }
}
