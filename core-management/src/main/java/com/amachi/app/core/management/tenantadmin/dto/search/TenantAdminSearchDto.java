package com.amachi.app.core.management.tenantadmin.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para administradores de tenant (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Hidden
public class TenantAdminSearchDto extends BaseSearchDto {
    private String tenantCode;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;
}
