package com.amachi.app.core.auth.dto.search;

import com.amachi.app.core.auth.enums.InvitationStatus;
import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para criterios de búsqueda de invitaciones (SaaS Elite Tier).
 * Jerarquía corregida: extends BaseSearchDto.
 */
@Getter
@Setter
@Schema(name = "UserInvitationSearch", description = "DTO to hold search criteria for UserInvitations")
public class UserInvitationSearchDto extends BaseSearchDto {

    @Schema(description = "Filter by Invitation Status")
    private InvitationStatus status;

    @Schema(description = "Filter by Role Name")
    private String roleName;

    @Schema(description = "Filter by Email")
    private String email;
}
