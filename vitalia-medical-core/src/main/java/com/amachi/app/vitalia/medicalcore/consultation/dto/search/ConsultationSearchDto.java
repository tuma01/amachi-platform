package com.amachi.app.vitalia.medicalcore.consultation.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.ConsultationStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ConsultationSearchDto extends BaseSearchDto {
    private Long patientId;
    private Long doctorId;
    private Long medicalHistoryId;
    private ConsultationStatus status;
    private OffsetDateTime dateFrom;
    private OffsetDateTime dateTo;
}
