package com.amachi.app.core.management.provisioning.service;

import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.dto.ProvisioningResult;

/**
 * Servicio de provisioning completo de usuarios.
 *
 * <p>Encapsula el ciclo de vida completo y ordenado:
 * <pre>
 *   Person → User → PersonTenant → UserAccount → UserTenantRole
 * </pre>
 *
 * <p>Todas las operaciones son <b>idempotentes</b>: si cualquier paso ya
 * fue ejecutado en una ejecución anterior, se detecta y se omite sin error.
 *
 * <p>Es el único lugar donde este ciclo debe implementarse.
 * Todos los flujos que necesiten provisionar un usuario (Bootstrap,
 * SuperAdminService, TenantAdminService) deben delegar aquí.
 */
public interface UserProvisioningService {

    /**
     * Ejecuta el ciclo completo de provisioning para el request dado.
     *
     * @param request parámetros del provisioning (email, tenant, rol, etc.)
     * @return resultado con las entidades resultantes
     */
    ProvisioningResult provision(ProvisioningRequest request);
}
