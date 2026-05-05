package com.amachi.app.vitalia.medicalcatalog.allergy.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para Alergias (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class AllergySearchDto extends BaseSearchDto {
    private String query;
    private String code;
    private String type;
    private String criticality;
}
