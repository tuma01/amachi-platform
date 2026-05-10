package com.amachi.app.vitalia.medicalcore.consultation.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.core.common.dto.PageResponseDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.ConsultationDto;
import com.amachi.app.vitalia.medicalcore.consultation.dto.search.ConsultationSearchDto;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;
import com.amachi.app.vitalia.medicalcore.consultation.mapper.ConsultationMapper;
import com.amachi.app.vitalia.medicalcore.consultation.service.ConsultationService;
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
@RequestMapping("/consultations")
@RequiredArgsConstructor
@Slf4j
public class ConsultationController extends BaseController implements ConsultationApi {

    private final ConsultationService service;
    private final ConsultationMapper mapper;

    @Override
    public ResponseEntity<ConsultationDto> getConsultationById(@NonNull Long id) {
        return ResponseEntity.ok(mapper.toDto(service.getById(id)));
    }

    @Override
    public ResponseEntity<ConsultationDto> createConsultation(@Valid @RequestBody @NonNull ConsultationDto dto) {
        return new ResponseEntity<>(mapper.toDto(service.create(dto)), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ConsultationDto> updateConsultation(@NonNull Long id, @Valid @RequestBody @NonNull ConsultationDto dto) {
        return ResponseEntity.ok(mapper.toDto(service.update(id, dto)));
    }

    @Override
    public ResponseEntity<Void> deleteConsultation(@NonNull Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ConsultationDto>> getAllConsultations() {
        return ResponseEntity.ok(service.getAll().stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<PageResponseDto<ConsultationDto>> getPaginatedConsultations(@NonNull ConsultationSearchDto searchDto, Integer pageIndex, Integer pageSize) {
        Page<Consultation> page = service.getAll(searchDto, pageIndex, pageSize);
        return ResponseEntity.ok(PageResponseDto.<ConsultationDto>builder()
                .content(page.getContent().stream().map(mapper::toDto).toList())
                .totalElements(page.getTotalElements()).pageIndex(page.getNumber())
                .pageSize(page.getSize()).totalPages(page.getTotalPages())
                .first(page.isFirst()).last(page.isLast())
                .empty(page.isEmpty()).numberOfElements(page.getNumberOfElements()).build());
    }

    @Override
    public ResponseEntity<List<ConsultationDto>> getConsultationsByPatient(@NonNull Long patientId) {
        return ResponseEntity.ok(service.getByPatientId(patientId).stream().map(mapper::toDto).toList());
    }

    @Override
    public ResponseEntity<List<ConsultationDto>> getConsultationsByMedicalHistory(@NonNull Long medicalHistoryId) {
        return ResponseEntity.ok(service.getByMedicalHistoryId(medicalHistoryId).stream().map(mapper::toDto).toList());
    }
}
