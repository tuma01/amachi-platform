package com.amachi.app.core.domain.context.service;

import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;

/**
 * Servicio de orquestación de contextos de usuario (SaaS Elite Tier).
 * Maneja la creación atómica de PersonTenant y entidades de dominio asociadas.
 */
public interface PersonContextService {

    /**
     * Crea un contexto completo para una persona dentro de un tenant.
     * Es una operación idempotente y transaccional.
     * 
     * @param person Identidad global.
     * @param tenant Inquilino de destino.
     * @param role Contexto de seguridad/navegación.
     * @param domain Contexto de entidad de dominio.
     */
    void createContext(Person person, Tenant tenant, RoleContext role, DomainContext domain);

    /**
     * Verifica si existe un contexto activo para la combinación especificada.
     * @return true si ya existe una relación activa.
     */
    boolean existsActiveContext(Person person, Tenant tenant, DomainContext domain);

    /**
     * Elimina (soft-delete) un contexto y sus entidades de dominio asociadas de forma sincronizada.
     */
    void removeContext(Person person, Tenant tenant, RoleContext role, DomainContext domain);
}
