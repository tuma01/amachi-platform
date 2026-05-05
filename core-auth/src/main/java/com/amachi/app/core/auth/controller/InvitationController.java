package com.amachi.app.core.auth.controller;

import com.amachi.app.core.auth.dto.invitation.CompleteRegistrationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationRequest;
import com.amachi.app.core.auth.dto.invitation.InvitationResponse;
import com.amachi.app.core.auth.service.InvitationService;
import com.amachi.app.core.common.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller implementing {@link InvitationApi}.
 *
 * <p>Delegates all business logic to {@link InvitationService}.
 * This class contains no business logic - it is responsible only for:
 * <ul>
 *     <li>HTTP method mapping (via {@link InvitationApi}).</li>
 *     <li>Request logging at INFO level.</li>
 *     <li>HTTP status code assignment.</li>
 * </ul>
 *
 * <p>Security:
 * <ul>
 *     <li>{@code GET  /auth/invitations}             - protected (TENANT_ADMIN role required).</li>
 *     <li>{@code POST /auth/invitations}             - protected (TENANT_ADMIN role required).</li>
 *     <li>{@code POST /auth/invitations/{id}/resend} - protected (TENANT_ADMIN role required).</li>
 *     <li>{@code GET  /auth/invitations/validate}    - public (no auth required).</li>
 *     <li>{@code POST /auth/invitations/accept}      - public (no auth required).</li>
 * </ul>
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class InvitationController implements InvitationApi {

    private final InvitationService invitationService;

    /**
     * {@inheritDoc}
     *
     * @return {@code 201 Created} with the invitation metadata.
     */
    @Override
    public ResponseEntity<InvitationResponse> createInvitation(InvitationRequest request) {
        log.info("📨 [POST /auth/invitations] Admin inviting email='{}'", request.getEmail());
        InvitationResponse response = invitationService.createInvitation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 200 OK} with invitation metadata if the token is valid.
     */
    @Override
    public ResponseEntity<InvitationResponse> validateToken(String token) {
        log.info("🔍 [GET /auth/invitations/validate] Validating token");
        InvitationResponse response = invitationService.validateToken(token);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 204 No Content} on successful onboarding.
     */
    @Override
    public ResponseEntity<Void> acceptInvitation(CompleteRegistrationRequest request) {
        log.info("🎉 [POST /auth/invitations/accept] User completing onboarding, loginEmail='{}'",
                request.getLoginEmail());
        invitationService.acceptInvitation(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 200 OK} alongside the standard PageResponseDto payload.
     */
    @Override
    public ResponseEntity<PageResponseDto<InvitationResponse>> getInvitations(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "pageIndex", defaultValue = "0", required = false) Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {

        log.info("📊 [GET /auth/invitations] Fetching paginated invitations, status='{}', role='{}'", status, role);

        Page<InvitationResponse> page = invitationService.getInvitations(status, role, pageIndex, pageSize);

        PageResponseDto<InvitationResponse> response = PageResponseDto.<InvitationResponse>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .pageIndex(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code 200 OK} holding the newly emitted token boundary metadata.
     */
    @Override
    public ResponseEntity<InvitationResponse> resendInvitation(@PathVariable("id") Long id) {
        log.info("🔄 [POST /auth/invitations/{}/resend] Resending invitation email", id);
        InvitationResponse response = invitationService.resendInvitation(id);
        return ResponseEntity.ok(response);
    }
}
