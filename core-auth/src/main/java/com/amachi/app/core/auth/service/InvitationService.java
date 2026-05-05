package com.amachi.app.core.auth.service;

import com.amachi.app.core.auth.dto.invitation.CompleteRegistrationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationResponse;
import org.springframework.data.domain.Page;

/**
 * Domain service interface for the user invitation lifecycle.
 *
 * <p>Orchestrates the two-step onboarding process:
 * <ol>
 *     <li><b>Step 1 (Admin)</b> — {@link #createInvitation(InvitationRequest)}:
 *         The TenantAdmin sends an invitation by submitting the recipient's email
 *         and the desired role. The system persists a {@code UserInvitation} record,
 *         generates a one-time URL, and dispatches the invitation email immediately.</li>
 *     <li><b>Step 2 (User)</b> — {@link #acceptInvitation(CompleteRegistrationRequest)}:
 *         The invited person clicks the link, fills in their personal details and credentials,
 *         and submits the form. The system validates the token, creates the {@code User} and
 *         {@code Person} accounts, and activates them immediately (auto-activation, Option A).</li>
 * </ol>
 */
public interface InvitationService {

    /**
     * Creates a user invitation and sends the activation email.
     *
     * <p>Validates that no duplicate PENDING invitation exists for the same
     * email + tenant combination before persisting.
     *
     * @param request  invitation data provided by the TenantAdmin
     * @return         response containing the created invitation metadata (no token exposed)
     * @throws com.amachi.app.core.auth.exception.AppSecurityException
     *         if a pending invitation already exists, the tenant is inactive,
     *         or the specified role does not exist
     */
    InvitationResponse createInvitation(InvitationRequest request);

    /**
     * Validates whether the given token corresponds to a PENDING,
     * non-expired {@code UserInvitation}.
     *
     * <p>Called by the frontend before rendering the onboarding form,
     * ensuring the user sees a clean error page if the link is stale.
     *
     * @param token  the raw UUID token extracted from the invitation URL
     * @return       invitation metadata if the token is valid
     * @throws com.amachi.app.core.auth.exception.AppSecurityException
     *         if the token is not found, already accepted, or expired
     */
    InvitationResponse validateToken(String token);

    /**
     * Accepts an invitation and auto-activates the new user account.
     *
     * <p>The method performs these operations atomically:
     * <ol>
     *     <li>Validates the token.</li>
     *     <li>Checks that the chosen login email is not already in use.</li>
     *     <li>Creates {@code Person} with the supplied personal details.</li>
     *     <li>Creates {@code User} with the definitive login email and hashed password.</li>
     *     <li>Creates {@code UserAccount} linking the user to the tenant.</li>
     *     <li>Assigns the pre-configured role via {@code UserTenantRole}.</li>
     *     <li>Marks the invitation as {@code ACCEPTED}.</li>
     *     <li>Sets {@code user.enabled = true} immediately (auto-activation).</li>
     * </ol>
     *
     * @param request  the onboarding form submitted by the invited person
     * @throws com.amachi.app.core.auth.exception.AppSecurityException
     *         if the token is invalid/expired, or the login email is already taken
     */
    void acceptInvitation(CompleteRegistrationRequest request);

    /**
     * Retrieves a paginated list of invitations for the current tenant context,
     * optionally filtered by status or role. Tenant isolation is guaranteed by
     * the Hibernate filter activated via {@code @TenantAware}.
     *
     * @param status     optional status filter (PENDING, ACCEPTED, etc.)
     * @param role       optional role name filter
     * @param pageIndex  zero-based pagination index
     * @param pageSize   amount of returned row blocks
     * @return a wrapped Page containing safe InvitationResponse data
     */
    Page<InvitationResponse> getInvitations(String status, String role, Integer pageIndex, Integer pageSize);

    /**
     * Resends an invitation email. Works by invalidating the past token entirely, 
     * forging a new secure tracking UUID and pushing expiration TTL constraints 
     * by +48 hours locally, before triggering SMTP protocols.
     *
     * @param id the internal primary key of the UserInvitation entity
     * @return an abstraction of the newly regenerated record
     * @throws com.amachi.app.core.auth.exception.AppSecurityException
     *         if ID is missing or target account has already consumed onboarding form
     */
    InvitationResponse resendInvitation(Long id);
}
