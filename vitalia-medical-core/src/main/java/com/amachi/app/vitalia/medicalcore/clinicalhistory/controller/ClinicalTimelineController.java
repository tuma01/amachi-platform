package com.amachi.app.vitalia.medicalcore.clinicalhistory.controller;

import com.amachi.app.core.common.controller.BaseController;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalEventDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalSummaryDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.service.ClinicalTimelineService;
import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Slf4j
public class ClinicalTimelineController extends BaseController implements ClinicalTimelineApi {

    private final ClinicalTimelineService service;

    @Override
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<ClinicalEventDto>> getTimeline(@NonNull Long patientId, ClinicalEventType type) {
        List<ClinicalEventDto> events = (type != null)
                ? service.getTimeline(patientId, type)
                : service.getTimeline(patientId);
        return ResponseEntity.ok(events);
    }

    @Override
    @PreAuthorize("hasAnyRole('DOCTOR', 'NURSE', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ClinicalSummaryDto> getSummary(@NonNull Long patientId) {
        return ResponseEntity.ok(service.getSummary(patientId));
    }
}
