package com.amachi.app.core.auth.config.multiTenant;

import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Resuelve el TenantId (Long) a partir del TenantCode (String) con caché TTL.
 *
 * Caffeine (TTL 5 min, max 500 entries) reemplaza al ConcurrentHashMap previo
 * para evitar que cambios en tenants (desactivación, renombramiento) queden
 * servidos indefinidamente desde caché hasta reinicio del servidor.
 *
 * SaaS Elite Tier — ISO 27001 A.9 / Multi-Tenant isolation.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantResolver {

    private final TenantRepository tenantRepository;

    private final Cache<String, Long> cache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(500)
            .build();

    /**
     * Resuelve el ID del tenant. Cache miss → DB lookup.
     * El TTL de 5 minutos garantiza que los cambios en tenants se reflejan
     * en menos de 5 minutos sin reinicio del servidor.
     */
    public Long resolveTenantId(String tenantCode) {
        if (tenantCode == null || tenantCode.isBlank()) {
            throw new RuntimeException("Missing tenant code in request");
        }

        return cache.get(tenantCode, code -> {
            log.debug("Cache miss para tenant [{}]. Buscando en DB...", code);
            Long id = tenantRepository.findByCode(code)
                    .map(Tenant::getId)
                    .orElseThrow(() -> new RuntimeException("Tenant no encontrado: " + code));
            log.debug("Tenant [{}] resuelto a ID [{}]", code, id);
            return id;
        });
    }

    public void evictCache(String tenantCode) {
        cache.invalidate(tenantCode);
    }

    public void clearCache() {
        cache.invalidateAll();
    }
}
