package com.amachi.app.core.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for UserAccount entity")
public class UserAccountDto {

    @Schema(description = "UserAccount ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Person ID", example = "1")
    private Long personId;

    @Schema(description = "Tenant name")
    private String tenantName;

    @Schema(description = "Roles assigned in this account")
    private Set<String> roles;
}
