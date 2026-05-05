package com.amachi.app.core.auth.service;

import com.amachi.app.core.auth.dto.JwtUserDto;
import com.amachi.app.core.common.dto.TokenPairDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface JwtService {

    /**
     * Genera un par completo de tokens (access + refresh) para un usuario
     */
    TokenPairDto generateTokenPair(JwtUserDto userDto);

    /**
     * Genera un token de activación de cuenta
     */
    String generateActivationToken(JwtUserDto userDto, String tenantCode);

    /**
     * Genera un token para restablecimiento de contraseña
     */
    String generatePasswordResetToken(JwtUserDto userDto);

    /**
     * Valida un token JWT
     */
    void validateToken(String token);

    /**
     * Extrae JwtUserDto del token (para tokens de acceso)
     */
    JwtUserDto extractUserDto(String token);

    /**
     * Extrae username/email del token
     */
    String extractUsername(String token);

    /**
     * Extrae userId del token
     */
    Long extractUserId(String token);

    /**
     * Extrae tenantCode del token
     */
    String extractTenantCode(String token);

    java.util.Date extractExpiration(String token);

    List<String> extractRoles(String token);

    /**
     * Verifica si el token está expirado
     */
    boolean isTokenExpired(String token);

    /**
     * Obtiene tiempo restante hasta expiración en segundos
     */
    long getRemainingTimeInSeconds(String token);

    boolean isPasswordResetToken(@NotEmpty(message = "Token is required") String token);

}
