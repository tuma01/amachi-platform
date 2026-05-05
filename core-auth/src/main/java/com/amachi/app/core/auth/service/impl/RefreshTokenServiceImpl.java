package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.entity.RefreshToken;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.auth.exception.TokenException;
import com.amachi.app.core.auth.repository.RefreshTokenRepository;
import com.amachi.app.core.auth.service.RefreshTokenService;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final com.amachi.app.core.auth.bridge.TenantBridge tenantBridge;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId, String tenantCode, String token) {
        User userRef = entityManager.getReference(User.class, userId);
        Tenant tenantRef = tenantBridge.findByCode(tenantCode);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRef)
                .tenantCode(tenantCode)
                .token(token)
                .expiryDate(Instant.now().plus(7, java.time.temporal.ChronoUnit.DAYS))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void verifyExpiration(RefreshToken token) {
        if (!token.isValid()) {
            refreshTokenRepository.delete(token);
            throw new TokenException("Refresh token expired. Please sign in again.", "REFRESH_TOKEN_EXPIRED");
        }
    }

    @Override
    @Transactional
    public void deleteByUserIdAndTenantCode(Long userId, String tenantCode) {
        refreshTokenRepository.deleteByUserIdAndTenantCode(userId, tenantCode);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteExpiredTokens();
    }

    @Override
    @Transactional
    public void delete(RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Scheduled(cron = "0 0 2 * * ?") // Cada día a las 2 AM
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("🧹 Cleaning up expired refresh tokens");
        deleteExpiredTokens();
    }
}
