package com.amachi.app.vitalia.medicalcatalog.kinship.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para Parentescos (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class KinshipSearchDto extends BaseSearchDto {
    private String query;
}
