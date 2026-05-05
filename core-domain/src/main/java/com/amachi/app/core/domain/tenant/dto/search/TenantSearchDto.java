package com.amachi.app.core.domain.tenant.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para tenants (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public final class TenantSearchDto extends BaseSearchDto {
    private String code;
    private String name;
    private String type;
    private Boolean isActive;
}
