package com.amachi.app.core.auth.config.multiTenant;

import com.amachi.app.core.auth.bridge.TenantBridge;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Caché de objetos Tenant completos, usado por JwtAuthenticationFilter
 * para validar la asociación usuario-tenant en cada request autenticado.
 *
 * Caffeine (TTL 5 min, max 500 entries) reemplaza al ConcurrentHashMap previo.
 * El TTL garantiza que si un tenant es desactivado, el sistema lo detectará
 * en un máximo de 5 minutos sin reinicio.
 *
 * SaaS Elite Tier — ISO 27001 A.9 / Multi-Tenant isolation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantCache {

    private final TenantBridge tenantBridge;

    private final Cache<String, Tenant> tenantMap = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(500)
            .build();

    public Tenant getTenant(String tenantCode) {
        return tenantMap.get(tenantCode, this::loadTenantFromDb);
    }

    public Long getTenantId(String tenantCode) {
        Tenant tenant = getTenant(tenantCode);
        return tenant != null ? tenant.getId() : null;
    }

    private Tenant loadTenantFromDb(String tenantCode) {
        log.info("Cargando Tenant [{}] desde DB", tenantCode);
        return tenantBridge.findByCode(tenantCode);
    }

    public void clearCache() {
        log.info("Limpiando cache de Tenants");
        tenantMap.invalidateAll();
    }

    public void evictTenant(String tenantCode) {
        log.info("Eliminando tenant [{}] del cache", tenantCode);
        tenantMap.invalidate(tenantCode);
    }
}
