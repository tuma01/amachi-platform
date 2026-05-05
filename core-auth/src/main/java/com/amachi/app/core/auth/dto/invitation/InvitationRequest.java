package com.amachi.app.core.auth.dto.invitation;

import com.amachi.app.core.common.enums.RoleContext;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO used by the TenantAdmin to send an invitation to a new staff member.
 * (Manual Implementation to ensure build stability)
 */
public class InvitationRequest {
    @NotBlank(message = "validation.invitation.email.required")
    @Email(message = "validation.invitation.email.invalid")
    @Size(max = 100, message = "validation.invitation.email.maxLength")
    private String email;

    @NotBlank(message = "validation.invitation.role.required")
    private String roleName;

    @NotNull(message = "validation.invitation.roleContext.required")
    private RoleContext roleContext;

    private String nationalId;

    @NotBlank(message = "validation.invitation.firstName.required")
    private String firstName;

    @NotBlank(message = "validation.invitation.lastName.required")
    private String lastName;

    public InvitationRequest() {}

    public InvitationRequest(String email, String roleName, RoleContext roleContext, String nationalId, String firstName, String lastName) {
        this.email = email;
        this.roleName = roleName;
        this.roleContext = roleContext;
        this.nationalId = nationalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public RoleContext getRoleContext() { return roleContext; }
    public void setRoleContext(RoleContext roleContext) { this.roleContext = roleContext; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public static InvitationRequestBuilder builder() {
        return new InvitationRequestBuilder();
    }

    public static class InvitationRequestBuilder {
        private String email;
        private String roleName;
        private RoleContext roleContext;
        private String nationalId;
        private String firstName;
        private String lastName;

        public InvitationRequestBuilder email(String email) { this.email = email; return this; }
        public InvitationRequestBuilder roleName(String roleName) { this.roleName = roleName; return this; }
        public InvitationRequestBuilder roleContext(RoleContext roleContext) { this.roleContext = roleContext; return this; }
        public InvitationRequestBuilder nationalId(String nationalId) { this.nationalId = nationalId; return this; }
        public InvitationRequestBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public InvitationRequestBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public InvitationRequest build() {
            return new InvitationRequest(email, roleName, roleContext, nationalId, firstName, lastName);
        }
    }
}
