package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.BlacklistedToken;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistedTokenRepository extends TenantCommonRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    void deleteByExpiresAtBefore(java.time.LocalDateTime now);
}
