package com.amachi.app.vitalia.medicalcore.clinicalhistory.service;

import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalEventDto;
import com.amachi.app.vitalia.medicalcore.clinicalhistory.dto.ClinicalSummaryDto;
import com.amachi.app.vitalia.medicalcore.common.enums.ClinicalEventType;

import java.util.List;

public interface ClinicalTimelineService {

    List<ClinicalEventDto> getTimeline(Long patientId);

    List<ClinicalEventDto> getTimeline(Long patientId, ClinicalEventType type);

    ClinicalSummaryDto getSummary(Long patientId);
}
