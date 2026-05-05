package com.amachi.app.core.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    @Email(message = "Email format is invalid")
    @NotEmpty(message = "Email is required")
    private String email;
}
