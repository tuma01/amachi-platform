package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.bridge.TenantBridge;
import com.amachi.app.core.auth.entity.BlacklistedToken;
import com.amachi.app.core.auth.entity.RefreshToken;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.auth.exception.TokenException;
import com.amachi.app.core.auth.repository.BlacklistedTokenRepository;
import com.amachi.app.core.auth.repository.UserTenantRoleRepository;
import com.amachi.app.core.auth.service.JwtService;
import com.amachi.app.core.auth.service.RefreshTokenService;
import com.amachi.app.core.auth.service.TokenService;
import com.amachi.app.core.auth.dto.JwtUserDto;
import com.amachi.app.core.common.dto.TokenPairDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final TenantBridge tenantBridge;

    @Override
    @Transactional
    public TokenPairDto generateAndStoreTokenPair(JwtUserDto userDto, User user, Tenant tenant) {
        // Generar par de tokens usando JwtService
        TokenPairDto tokenPair = jwtService.generateTokenPair(userDto);

        // Guardar refresh token en base de datos
        refreshTokenService.createRefreshToken(
                user.getId(),
                tenant.getCode(),
                tokenPair.getRefreshToken());

        return tokenPair;
    }

    @Override
    @Transactional
    public TokenPairDto refreshTokenPair(String refreshToken) {

        // 1. Buscar refresh token
        RefreshToken token = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() ->
                        new TokenException("Refresh token not found", "REFRESH_TOKEN_NOT_FOUND"));

        if (token.getTenantCode() == null) {
            throw new TokenException("Refresh token missing tenant", "INVALID_REFRESH_TOKEN");
        }
        // 2. Validar expiración + estado
        refreshTokenService.verifyExpiration(token);

        if (!token.isValid()) {
            throw new TokenException("Refresh token invalid", "INVALID_REFRESH_TOKEN");
        }

        // 3. Resolver entidades
        User user = token.getUser();
        Tenant tenant = tenantBridge.findByCode(token.getTenantCode());

        // 4. Cargar roles y permisos
        List<String> roles = userTenantRoleRepository.findActiveRolesByUserAndTenant(user, tenant);
        List<String> permissions = userTenantRoleRepository.findActivePermissionsNamesByUserAndTenant(user, tenant);

        List<String> combinedAuthorities = new ArrayList<>(roles);
        combinedAuthorities.addAll(permissions);
        combinedAuthorities = combinedAuthorities.stream().distinct().toList();

        // 5. Construir JWT user
        JwtUserDto jwtUser = JwtUserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .tenantCode(tenant.getCode())
                .roles(combinedAuthorities)
                .build();

        // 6. Generar nuevo token (PRIMERO)
        TokenPairDto newTokenPair = generateAndStoreTokenPair(jwtUser, user, tenant);

        // 7. Invalidar el anterior (DESPUÉS)
        refreshTokenService.delete(token);

        // 8. Retornar
        return newTokenPair;
    }

    @Override
    @Transactional
    public void invalidateUserTokens(Long userId, String tenantCode) {
        refreshTokenService.deleteByUserIdAndTenantCode(userId, tenantCode);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateRefreshToken(String refreshToken) {
        Optional<RefreshToken> storedToken = refreshTokenService.findByToken(refreshToken);
        if (storedToken.isEmpty()) {
            return false;
        }

        try {
            refreshTokenService.verifyExpiration(storedToken.get());
            return true;
        } catch (TokenException e) {
            return false;
        }
    }

    /**
     * Invalida un JWT agregándolo a la blacklist.
     */
    /**
     * Invalida un JWT agregándolo a la blacklist con su fecha de expiración.
     */
    public void invalidateToken(String jwt) {
        if (jwt == null || jwt.isBlank())
            return;

        java.util.Date expirationDate = jwtService.extractExpiration(jwt);
        // Convertir Date a LocalDateTime
        java.time.LocalDateTime expiresAt = expirationDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime();

        BlacklistedToken token = BlacklistedToken.builder()
                .token(jwt)
                .blacklistedAt(java.time.LocalDateTime.now())
                .expiresAt(expiresAt)
                .build();

        blacklistedTokenRepository.save(token);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens(java.time.LocalDateTime now) {
        blacklistedTokenRepository.deleteByExpiresAtBefore(now);
    }

    /**
     * Verifica si un JWT está en la blacklist
     */
    public boolean isTokenBlacklisted(String jwt) {
        return blacklistedTokenRepository.existsByToken(jwt);
    }
}
