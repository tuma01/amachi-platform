package com.amachi.app.vitalia.medicalcore.hospitalization.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DischargeMedicationSearchDto extends BaseSearchDto {
    private Long hospitalizationId;
    private Long medicationId;
}
