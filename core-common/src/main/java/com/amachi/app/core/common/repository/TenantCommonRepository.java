package com.amachi.app.core.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

/**
 * Interface para repositorios que requieren aislamiento por Tenant (Multi-tenant).
 *
 * @param <E> Tipo de la Entidad
 * @param <ID> Tipo del ID
 */
@NoRepositoryBean
public interface TenantCommonRepository<E, ID> extends CommonRepository<E, ID> {

    /**
     * Búsqueda segura por ID y Tenant (SaaS Ready).
     */
    Optional<E> findByIdAndTenantId(ID id, Long tenantId);

    /**
     * Verifica la existencia por ID y Tenant.
     */
    boolean existsByIdAndTenantId(ID id, Long tenantId);
}
