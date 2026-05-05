package com.amachi.app.core.auth.dto;

import com.amachi.app.core.common.dto.TokenPairDto;
import com.amachi.app.core.common.dto.UserSummaryDto;
import lombok.*;

/**
 * Authentication response (SaaS Elite Tier).
 * Clean contract: all token data is inside the 'tokens' object (TokenPairDto).
 * User identity data is inside the 'user' object (UserSummaryDto).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private TokenPairDto tokens;
    private UserSummaryDto user;
}
