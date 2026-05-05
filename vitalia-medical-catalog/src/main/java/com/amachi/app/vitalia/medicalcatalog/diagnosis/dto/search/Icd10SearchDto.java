package com.amachi.app.vitalia.medicalcatalog.diagnosis.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para CIE-10 (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class Icd10SearchDto extends BaseSearchDto {
    private String query;
    private String code;
}
