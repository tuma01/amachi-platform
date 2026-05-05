package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.RefreshToken;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends TenantCommonRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserIdAndTenantCode(Long userId, String tenantCode);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();

    @Modifying
    void deleteByUserIdAndTenantCode(Long userId, String tenantCode);

}
