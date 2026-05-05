package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.PasswordResetToken;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.repository.TenantCommonRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends TenantCommonRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByExpirationDateBefore(java.time.Instant now);
}
