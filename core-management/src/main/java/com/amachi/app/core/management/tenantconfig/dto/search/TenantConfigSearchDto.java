package com.amachi.app.core.management.tenantconfig.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para configuración de tenant (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class TenantConfigSearchDto extends BaseSearchDto {
    private String fallbackHeader;
    private String defaultDomain;
    private Long tenantId;
}
