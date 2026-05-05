package com.amachi.app.core.auth.dto;

import com.amachi.app.core.common.dto.RoleDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserTenantRole", description = "Schema to hold UserTenantRole information")
public class UserTenantRoleDto {

    @Schema(description = "Identificador unico", example = "1")
    private Long id;

    @Schema(description = "User associated with the role assignment")
    private UserDto user;

    @Schema(description = "Role assigned to the user in the tenant context")
    private RoleDto role;

    @Schema(description = "Fecha en que se asignó el rol", example = "2024-01-01T12:00:00")
    private LocalDateTime assignedAt;

    @Schema(description = "Fecha en que se revocó el rol", example = "2024-06-01T12:00:00")
    private LocalDateTime revokedAt;

    @Schema(description = "Estado del rol (activo/inactivo)", example = "true")
    @Builder.Default
    private boolean active = true;
}
