package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.ActivationToken;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationTokenRepository extends TenantCommonRepository<ActivationToken, Long> {
    Optional<ActivationToken> findByToken(String token);
}
