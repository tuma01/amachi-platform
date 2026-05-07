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

/**
 * Bootstrap factory que inicializa los tenants de sistema desde AppBootstrapProperties.
 * Disponible en todos los módulos via core-common.
 * SaaS Elite Tier — Multi-Tenant infrastructure.
 */
@Component
@Slf4j
public class TenantFactory {

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
        var g = props.getTenant().getTenantGlobal();
        globalTenant = TenantInfo.builder()
                .code(g.getCode())
                .name(g.getName())
                .type(TenantType.valueOf(g.getType()))
                .description(g.getDescription())
                .build();
        tenantMap.put(g.getCode(), globalTenant);

        var l = props.getTenant().getTenantLocal();
        localTenant = TenantInfo.builder()
                .code(l.getCode())
                .name(l.getName())
                .type(TenantType.valueOf(l.getType()))
                .fallbackHeader(l.getFallbackHeader())
                .build();
        tenantMap.put(l.getCode(), localTenant);

        log.info("TenantFactory inicializado: {}", tenantMap.keySet());
    }

    public TenantInfo getTenant(String code) {
        TenantInfo tenant = tenantMap.get(code);
        if (tenant == null) {
            throw new IllegalArgumentException("Tenant no encontrado: " + code);
        }
        return tenant;
    }

    public boolean exists(String code) {
        return tenantMap.containsKey(code);
    }

    public Map<String, TenantInfo> getAll() {
        return Collections.unmodifiableMap(tenantMap);
    }

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
    }
}
