package com.amachi.app.core.auth.dto.search;

import com.amachi.app.core.auth.dto.UserDto;
import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Filtros de búsqueda para privilegios de usuario en tenants (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UserTenantRoleSearchDto extends BaseSearchDto {
    private UserDto user;
    private LocalDateTime assignedAt;
    private LocalDateTime revokedAt;
    @Builder.Default
    private Boolean active = true;
}
