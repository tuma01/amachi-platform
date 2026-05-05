package com.amachi.app.core.management.provisioning.dto;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import lombok.Builder;
import lombok.Value;

/**
 * Parámetros de entrada para el ciclo completo de provisioning de un usuario.
 * Todos los campos obligatorios: email, tenant, roleContext, domainContext.
 * Si person/user ya existen (ej. desde un flujo de controller), se pasan directamente
 * para evitar re-resolución innecesaria.
 */
@Value
@Builder
public class ProvisioningRequest {

    /** Email del usuario — clave de identidad global. */
    String email;

    /** Password en texto plano. Si es null, el usuario ya existe y no se modifica. */
    String rawPassword;

    /** Nombre. Puede ser null si el usuario ya existe. */
    String firstName;

    /** Apellido. Puede ser null si el usuario ya existe. */
    String lastName;

    /** Tenant objetivo del provisioning. Obligatorio. */
    Tenant tenant;

    /** Contexto de rol (SUPER_ADMIN, ADMIN, DOCTOR, PATIENT, etc.). */
    RoleContext roleContext;

    /** Contexto de dominio para PersonTenant. */
    DomainContext domainContext;

    /**
     * Person ya resuelta (opcional). Si se provee, se usa directamente
     * sin pasar por IdentityResolutionService.
     */
    Person resolvedPerson;

    /**
     * User ya resuelto (opcional). Si se provee, se usa directamente
     * sin re-crear ni re-buscar.
     */
    User resolvedUser;
}
