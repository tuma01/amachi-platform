package com.amachi.app.core.auth.dto.invitation;

import com.amachi.app.core.auth.enums.InvitationStatus;
import com.amachi.app.core.common.enums.RoleContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO returned after creating or querying a {@code UserInvitation}.
 *
 * <p>Returned by {@code POST /auth/invitations} and {@code GET /auth/invitations/validate}.
 * Exposes only safe, non-sensitive data to the client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Schema(name = "InvitationResponse",
        description = "Response payload carrying the result of an invitation operation.")
public class InvitationResponse {

    /**
     * Primary key of the created invitation record.
     */
    @Schema(description = "Internal ID of the invitation.", example = "42")
    private Long id;

    /**
     * Email address to which the invitation was sent.
     */
    @Schema(description = "Email that received the invitation link.", example = "maria.garcia@gmail.com")
    private String email;

    /**
     * Pre-assigned role name (e.g. ROLE_DOCTOR).
     */
    @Schema(description = "Role that will be granted upon acceptance.", example = "ROLE_DOCTOR")
    private String roleName;

    /**
     * Human-readable name of the tenant for display purposes.
     */
    @Schema(description = "Name of the target hospital/clinic.", example = "Hospital San Borja")
    private String tenantName;

    /**
     * Current lifecycle status of the invitation.
     */
    @Schema(description = "Current status of the invitation.", example = "PENDING")
    private InvitationStatus status;

    /**
     * Timestamp at which the invitation was created.
     */
    @Schema(description = "Creation timestamp.")
    private LocalDateTime createdAt;

    /**
     * Timestamp at which the invitation token will expire.
     */
    @Schema(description = "Expiration timestamp of the one-time link.")
    private LocalDateTime expiresAt;

    /**
     * Functional context assigned to this invitation.
     */
    @Schema(description = "Role context for the invitee.", example = "DOCTOR")
    private RoleContext roleContext;
}
