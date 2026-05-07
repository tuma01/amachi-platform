package com.amachi.app.core.domain.hospital.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para hospitales (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public final class HospitalSearchDto extends BaseSearchDto {
    private String legalName;
    private String taxId;
    private String medicalLicense;
}
