package com.amachi.app.vitalia.medicalcatalog.specialty.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para Especialidades (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class MedicalSpecialtySearchDto extends BaseSearchDto {
    private String query;
    private String code;
}
