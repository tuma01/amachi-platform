package com.amachi.app.vitalia.medicalcatalog.consultation.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Search filters for Medical Consultation Types (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class MedicalConsultationTypeSearchDto extends BaseSearchDto {
    private String query;
    private Long specialtyId;
}
