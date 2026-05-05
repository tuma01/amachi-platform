package com.amachi.app.core.geography.address.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para direcciones (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public final class AddressSearchDto extends BaseSearchDto {
    private String addressLine;
    private String city;
    private String zipCode;
    private Long countryId;
    private Long stateId;
    private Long provinceId;
    private Long municipalityId;
}
