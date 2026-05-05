package com.amachi.app.core.auth.identity.service;

import com.amachi.app.core.domain.entity.Person;

/**
 * Servicio de resolución y unificación de identidad (SaaS Elite Tier).
 * Garantiza que un ser humano sea reconocido de forma única en toda la plataforma.
 */
public interface IdentityResolutionService {

    /**
     * Resuelve una identidad existente o crea una nueva de forma atómica.
     * 
     * @param nationalId DNI, Pasaporte o Cédula (Ancla universal).
     * @param email Email de contacto (Ancla de comunicación).
     * @param personData Datos iniciales en caso de creación.
     * @return Person persistida.
     */
    Person resolveOrCreateIdentity(String nationalId, String email, Person personData);
}
