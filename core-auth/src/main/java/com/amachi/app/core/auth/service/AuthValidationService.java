package com.amachi.app.core.auth.service;

/**
 * Servicio de validación de seguridad (SaaS Ready).
 */
public interface AuthValidationService {

    /**
     * Valida que un usuario tenga acceso a un tenant específico.
     * @param userId ID del usuario (AUT_USER).
     * @param tenantId ID del tenant (DMN_TENANT).
     * @throws RuntimeException si la validación falla.
     */
    void validateUserTenant(Long userId, Long tenantId);
}
