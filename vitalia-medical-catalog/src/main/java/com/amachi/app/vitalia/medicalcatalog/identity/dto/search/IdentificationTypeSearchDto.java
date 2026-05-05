package com.amachi.app.vitalia.medicalcatalog.identity.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para Tipos de Identificación (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class IdentificationTypeSearchDto extends BaseSearchDto {
    private String query;
}
