package com.amachi.app.core.geography.municipality.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para municipios (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public final class MunicipalitySearchDto extends BaseSearchDto {
    private String name;
    private Long provinceId;
    private Long stateId;
    private Long countryId;
}
