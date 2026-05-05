package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.service.JwtService;
import com.amachi.app.core.auth.dto.JwtUserDto;
import com.amachi.app.core.common.dto.TokenPairDto;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.access-token.expiration:3600000}")
    private long accessTokenExpiration;

    @Value("${security.jwt.refresh-token.expiration:604800000}")
    private long refreshTokenExpiration;

    @Value("${security.jwt.activation-token.expiration:86400000}")
    private long activationTokenExpiration;

    @Value("${security.jwt.password-reset-token.expiration:1800000}")
    private long passwordResetTokenExpiration;

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_TENANT_CODE = "tenantCode";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
            log.info("✅ JWT Service initialized successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize JWT Service", e);
            throw new IllegalStateException("JWT initialization failed", e);
        }
    }

    @Override
    public TokenPairDto generateTokenPair(JwtUserDto userDto) {
        String accessToken = generateAccessToken(userDto);
        Instant accessTokenExpiresAt = Instant.now().plusMillis(accessTokenExpiration);
        String refreshToken = UUID.randomUUID().toString();
        Instant refreshTokenExpiresAt = Instant.now().plusMillis(refreshTokenExpiration);

        return TokenPairDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiresAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .refreshTokenExpiresAt(refreshTokenExpiresAt)
                .expiresIn(accessTokenExpiration / 1000L)
                .build();
    }

    @Override
    public String generateActivationToken(JwtUserDto userDto, String tenantCode) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userDto.getUserId());
        claims.put(CLAIM_TENANT_CODE, tenantCode);
        claims.put(CLAIM_TOKEN_TYPE, "activation");

        return buildToken(claims, userDto.getEmail(), activationTokenExpiration);
    }

    @Override
    public String generatePasswordResetToken(JwtUserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userDto.getUserId());
        claims.put(CLAIM_TOKEN_TYPE, "password_reset");

        return buildToken(claims, userDto.getEmail(), passwordResetTokenExpiration);
    }

    @Override
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token expired: {}", ex.getMessage());
            throw new TokenException("Token expired", "TOKEN_EXPIRED");
        } catch (JwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
            throw new TokenException("Invalid token", "INVALID_TOKEN");
        }
    }

    @Override
    public JwtUserDto extractUserDto(String token) {
        Claims claims = extractAllClaims(token);
        String tokenType = claims.get(CLAIM_TOKEN_TYPE, String.class);
        if (!"access".equals(tokenType)) {
            throw new TokenException("Invalid token type for user extraction", "INVALID_TOKEN_TYPE");
        }

        return JwtUserDto.builder()
                .userId(claims.get(CLAIM_USER_ID, Long.class))
                .email(claims.getSubject())
                .tenantCode(claims.get(CLAIM_TENANT_CODE, String.class))
                .roles(extractRoles(token))
                .build();
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_USER_ID, Long.class);
    }

    @Override
    public String extractTenantCode(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get(CLAIM_TENANT_CODE, String.class);
    }

    @Override
    public java.util.Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public List<String> extractRoles(String token) {
        Object rolesObj = extractAllClaims(token).get("roles");
        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .toList();
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public long getRemainingTimeInSeconds(String token) {
        Date expiration = extractExpiration(token);
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public boolean isPasswordResetToken(String token) {
        Claims claims = extractAllClaims(token);
        return "password_reset".equals(claims.get(CLAIM_TOKEN_TYPE, String.class));
    }

    // ========== MÉTODOS PRIVADOS ==========

    private String generateAccessToken(JwtUserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USER_ID, userDto.getUserId());
        claims.put(CLAIM_TENANT_CODE, userDto.getTenantCode());
        claims.put(CLAIM_ROLES, userDto.getRoles());
        claims.put(CLAIM_TOKEN_TYPE, "access");

        return buildToken(claims, userDto.getEmail(), accessTokenExpiration);
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiration) {
//        return Jwts.builder()
//                .claims(claims)
//                .subject(subject)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(signingKey)
//                .compact();
        return Jwts.builder()
                .setClaims(claims) // CAMBIO 0.11.5: antes era claims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256) // CAMBIO 0.11.5: antes solo signWith(signingKey)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
//            return Jwts.parser()
//                    .verifyWith(signingKey)
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("Error extracting claims from token: {}", e.getMessage());
            throw new TokenException("Failed to extract token claims", "CLAIMS_EXTRACTION_FAILED");
        }
    }

    public static class TokenException extends RuntimeException {
        private final String errorCode;

        public TokenException(String message, String errorCode) {
            super(message);
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}
