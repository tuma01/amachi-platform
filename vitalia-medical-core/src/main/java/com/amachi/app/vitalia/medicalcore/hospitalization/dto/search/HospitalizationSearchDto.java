package com.amachi.app.vitalia.medicalcore.hospitalization.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HospitalizationSearchDto extends BaseSearchDto {
    private Long patientId;
    private Long doctorId;
    private Long unitId;
    private Long bedId;
    private HospitalizationStatus status;
    private OffsetDateTime admissionDateFrom;
    private OffsetDateTime admissionDateTo;
}
