package com.amachi.app.core.auth.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

/**
 * Filtros de búsqueda para roles (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class RoleSearchDto extends BaseSearchDto {
    private String name;
}
