package com.amachi.app.core.common.tenant.factory;

import com.amachi.app.core.common.config.AppBootstrapProperties;
import com.amachi.app.core.common.enums.TenantType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class TenantFactory {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TenantFactory.class);

    private final AppBootstrapProperties props;

    @Getter
    private TenantInfo globalTenant;

    @Getter
    private TenantInfo localTenant;

    private final Map<String, TenantInfo> tenantMap = new HashMap<>();

    public TenantFactory(AppBootstrapProperties props) {
        this.props = props;
    }

    @PostConstruct
    public void init() {
        // -----------------------------
        // 1️⃣ GLOBAL TENANT
        // -----------------------------
        var g = props.getTenant().getTenantGlobal();
        globalTenant = new TenantInfo(
                g.getCode(),
                g.getName(),
                TenantType.valueOf(g.getType()),
                g.getDescription(),
                null);
        tenantMap.put(g.getCode(), globalTenant);

        // -----------------------------
        // 2️⃣ LOCAL TENANT
        // -----------------------------
        var l = props.getTenant().getTenantLocal();
        localTenant = new TenantInfo(
                l.getCode(),
                l.getName(),
                TenantType.valueOf(l.getType()),
                null,
                l.getFallbackHeader());
        tenantMap.put(l.getCode(), localTenant);

        log.info("📦 TenantFactory inicializado con tenants: {}", tenantMap.keySet());
    }

    // -----------------------------
    // Búsqueda y validación
    // -----------------------------

    /**
     * Devuelve el tenant por código, lanza excepción si no existe.
     */
    public TenantInfo getTenant(String code) {
        TenantInfo tenant = tenantMap.get(code);
        if (tenant == null) {
            throw new RuntimeException("Tenant no encontrado: " + code);
        }
        return tenant;
    }

    /**
     * Devuelve true si el tenant existe.
     */
    public boolean exists(String code) {
        return tenantMap.containsKey(code);
    }

    /**
     * Devuelve un mapa inmutable de todos los tenants.
     */
    public Map<String, TenantInfo> getAll() {
        return Collections.unmodifiableMap(tenantMap);
    }

    /**
     * Clase para encapsular la información de cada tenant.
     * (SaaS Elite Tier - Class version for compatibility)
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TenantInfo {
        private String code;
        private String name;
        private TenantType type;
        private String description;
        private String fallbackHeader;

        // Puente para compatibilidad con sintaxis de record
        public String code() { return code; }
        public String fallbackHeader() { return fallbackHeader; }
    }
}
