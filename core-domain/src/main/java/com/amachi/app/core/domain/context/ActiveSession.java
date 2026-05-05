package com.amachi.app.core.domain.context;

import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Representa el contexto de sesión activa del usuario (SaaS Elite Tier).
 * Centraliza el tenant actual y el rol (UX y Dominio) seleccionado para la navegación.
 * 
 * NOTA: Esta clase fue movida a core-domain para permitir la referencia directa
 * a la entidad Tenant, respetando la modularidad del sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveSession {

    /**
     * El Tenant (Hospital/Clínica) que el usuario está operando actualmente.
     */
    private Tenant tenant;

    /**
     * El contexto de navegación/UX seleccionado (ej. DOCTOR, PATIENT).
     */
    private RoleContext roleContext;

    /**
     * El contexto de dominio persistente derivado del roleContext.
     */
    private DomainContext domainContext;

    /**
     * ID de la persona física vinculada a esta sesión.
     */
    private Long personId;

    /**
     * ID del usuario de seguridad vinculado a esta sesión.
     */
    private Long userId;

    /**
     * El código identificador del tenant (alias).
     */
    public String getTenantCode() {
        return tenant != null ? tenant.getCode() : null;
    }
}
