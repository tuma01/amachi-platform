package com.amachi.app.vitalia.medicalcore.appointment.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentReminderDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentReminderSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;
import com.amachi.app.vitalia.medicalcore.appointment.mapper.AppointmentReminderMapper;
import com.amachi.app.vitalia.medicalcore.appointment.service.impl.AppointmentReminderServiceImpl;
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
@RequestMapping("/appointment-reminders")
@RequiredArgsConstructor
@Slf4j
public class AppointmentReminderController extends BaseController implements AppointmentReminderApi {

    private final AppointmentReminderServiceImpl service;
    private final AppointmentReminderMapper mapper;

    @Override
    public ResponseEntity<AppointmentReminderDto> getAppointmentReminderById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<AppointmentReminderDto> createAppointmentReminder(@Valid @RequestBody @NonNull AppointmentReminderDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AppointmentReminderDto> updateAppointmentReminder(@NonNull Long id, @Valid @RequestBody @NonNull AppointmentReminderDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteAppointmentReminder(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<AppointmentReminderDto>> getAllAppointmentReminders() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<AppointmentReminderDto>> getPaginatedAppointmentReminders(
            @NonNull AppointmentReminderSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<AppointmentReminder> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<AppointmentReminderDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }
}
