package com.amachi.app.core.auth.config.multiTenant;

import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resuelve el TenantId (Long) a partir del TenantCode (String) con soporte de caché.
 * SaaS Elite Tier: Optimización de performance para aislamiento de datos.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TenantResolver {

    private final TenantRepository tenantRepository;
    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    /**
     * Resuelve el ID del tenant. 
     * Si no se encuentra en caché, lo busca en base de datos.
     * 
     * @param tenantCode Código alfanumérico del tenant (ej: hospital-a).
     * @return ID numérico del tenant.
     */
    public Long resolveTenantId(String tenantCode) {
        if (tenantCode == null || tenantCode.isBlank()) {
            throw new RuntimeException("Missing tenant code in request");
        }

        return cache.computeIfAbsent(tenantCode, code -> {
            log.debug("🔍 Cache miss para tenant [{}]. Buscando en DB...", code);
            Long id = tenantRepository.findByCode(code)
                    .map(Tenant::getId)
                    .orElseThrow(() -> new RuntimeException("Tenant no encontrado para el código: " + code));
            
            log.debug("✅ Tenant [{}] resuelto a ID [{}]", code, id);
            return id;
        });
    }

    /**
     * Limpia la caché si hay cambios en los tenants.
     */
    public void evictCache(String tenantCode) {
        cache.remove(tenantCode);
    }

    public void clearCache() {
        cache.clear();
    }
}
