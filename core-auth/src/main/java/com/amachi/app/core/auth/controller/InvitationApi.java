package com.amachi.app.core.auth.controller;

import com.amachi.app.core.auth.dto.invitation.CompleteRegistrationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationResponse;
import com.amachi.app.core.common.dto.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API contract for the user invitation lifecycle.
 *
 * <p>Base path: {@code /auth/invitations}
 *
 * <p>Endpoints:
 * <ul>
 *     <li>{@code GET  /}               - Admin lists paginated invitations for their tenant.</li>
 *     <li>{@code POST /}               - Admin creates and dispatches an invitation.</li>
 *     <li>{@code POST /{id}/resend}    - Admin resends a pending or expired invitation.</li>
 *     <li>{@code GET  /validate}       - Public token validation before showing the form.</li>
 *     <li>{@code POST /accept}         - User submits the onboarding form to activate account.</li>
 * </ul>
 * 
 * <p>Note: The base path does not enforce security rules; method-level security 
 * should be handled at the Gateway or via Spring Security configuration.
 */
@Tag(
    name = "User Invitations",
    description = "REST API for the two-step user onboarding flow: " +
                  "Admin sends invitation → User completes self-registration."
)
@RequestMapping(value = "/auth/invitations", produces = MediaType.APPLICATION_JSON_VALUE)
public interface InvitationApi {

    String NAME_API = "Invitation";

    // ─────────────────────────────────────────────────────────────────────
    // POST /auth/invitations  — Admin creates invitation
    // ─────────────────────────────────────────────────────────────────────

    @Operation(
        summary = "Send a user invitation [TENANT_ADMIN]",
        description = "Creates a pending " + NAME_API + " and sends an activation email " +
                      "to the specified address. Requires TENANT_ADMIN role.",
        responses = {
            @ApiResponse(responseCode = "201", description = NAME_API + " created and email dispatched."),
            @ApiResponse(responseCode = "400", description = "Invalid request data (missing fields, bad email format)."),
            @ApiResponse(responseCode = "409", description = "A pending invitation already exists for this email in the tenant."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
        }
    )
    @PostMapping
    ResponseEntity<InvitationResponse> createInvitation(
            @Parameter(description = "Invitation details provided by the TenantAdmin.", required = true)
            @Valid @RequestBody InvitationRequest request
    );

    // ─────────────────────────────────────────────────────────────────────
    // GET /auth/invitations/validate?token=  — Public token check
    // ─────────────────────────────────────────────────────────────────────

    @Operation(
        summary = "Validate an invitation token [PUBLIC]",
        description = "Verifies that the given token corresponds to a PENDING, non-expired " +
                      NAME_API + ". Called by the frontend before rendering the onboarding form.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Token is valid. Returns invitation metadata."),
            @ApiResponse(responseCode = "400", description = "Token parameter missing."),
            @ApiResponse(responseCode = "404", description = "Token not found, expired, or already accepted."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
        }
    )
    @GetMapping("/validate")
    ResponseEntity<InvitationResponse> validateToken(
            @Parameter(description = "One-time token from the invitation URL.", required = true)
            @RequestParam("token") String token
    );

    // ─────────────────────────────────────────────────────────────────────
    // POST /auth/invitations/accept  — User completes onboarding
    // ─────────────────────────────────────────────────────────────────────

    @Operation(
        summary = "Accept invitation and complete self-registration [PUBLIC]",
        description = "The invited person submits their personal details and credentials. " +
                      "The system creates the User + Person accounts and activates them immediately " +
                      "(auto-activation — no additional Admin approval required).",
        responses = {
            @ApiResponse(responseCode = "204", description = "Onboarding complete. Account activated."),
            @ApiResponse(responseCode = "400", description = "Invalid form data (missing required fields, weak password, bad email)."),
            @ApiResponse(responseCode = "404", description = "Token not found, expired, or already accepted."),
            @ApiResponse(responseCode = "409", description = "The chosen login email is already registered in the system."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
        }
    )
    @PostMapping("/accept")
    ResponseEntity<Void> acceptInvitation(
            @Parameter(description = "Self-onboarding form submitted by the invited person.", required = true)
            @Valid @RequestBody CompleteRegistrationRequest request
    );

    // ---------------------------------------------------------------------
    // GET /auth/invitations  - Admin views dashboard grid
    // ---------------------------------------------------------------------

    @Operation(
        summary = "Get paginated invitations [TENANT_ADMIN]",
        description = "Retrieves a paginated list of invitations for the current tenant (resolved from subdomain). " +
                      "Useful for rendering the invitations administrative dashboard.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Paginated list of invitations successfully retrieved."),
            @ApiResponse(responseCode = "403", description = "Not authorized to read this tenant's data."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
        }
    )
    @GetMapping
    ResponseEntity<PageResponseDto<InvitationResponse>> getInvitations(
            @Parameter(description = "Optional filter by invitation status.")
            @RequestParam(value = "status", required = false) String status,
            @Parameter(description = "Optional filter by role name.")
            @RequestParam(value = "role", required = false) String role,
            @Parameter(description = "Zero-based page index.", example = "0")
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @Parameter(description = "Size of the records per page.", example = "10")
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize
    );

    // ---------------------------------------------------------------------
    // POST /auth/invitations/{id}/resend  - Admin resends email
    // ---------------------------------------------------------------------

    @Operation(
        summary = "Resend invitation email [TENANT_ADMIN]",
        description = "Generates a new secure token, pushes back the expiration date by 48 hours, " +
                      "and dispatches a fresh email to the original recipient. " +
                      "Applicable for PENDING or EXPIRED invitations; fails if ACCEPTED.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Invitation resent and metadata updated."),
            @ApiResponse(responseCode = "400", description = "Cannot resend an already ACCEPTED invitation."),
            @ApiResponse(responseCode = "404", description = "Invitation ID not found in the Database."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
        }
    )
    @PostMapping("/{id}/resend")
    ResponseEntity<InvitationResponse> resendInvitation(
            @Parameter(description = "The database ID of the target UserInvitation to resend.", required = true)
            @PathVariable("id") Long id
    );
}
