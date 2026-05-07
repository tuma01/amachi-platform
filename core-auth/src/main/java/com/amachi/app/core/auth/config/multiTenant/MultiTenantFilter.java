package com.amachi.app.core.auth.config.multiTenant;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class MultiTenantFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantFilter.class);
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * Rutas que no requieren resolución de tenant:
     * infraestructura técnica (docs, health) y endpoints globales sin scope de tenant.
     */
    private static final List<String> NO_TENANT_PATHS = List.of(
            "/tenants/**",
            "/themes/tenant/**",
            "/public/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**"
    );

    private final TenantResolver tenantResolver;
    private final TenantCache tenantCache;

    public MultiTenantFilter(@Lazy TenantResolver tenantResolver,
                             @Lazy TenantCache tenantCache) {
        this.tenantResolver = tenantResolver;
        this.tenantCache = tenantCache;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        boolean skip = NO_TENANT_PATHS.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
        if (skip) {
            log.debug("MultiTenantFilter skipped for: {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String tenantCode = extractTenantCode(request);

            if (tenantCode == null || tenantCode.isBlank()) {
                log.warn("Tenant no resuelto para: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Tenant could not be resolved. Ensure you are using a valid subdomain.");
                return;
            }

            // Validar existencia y estado activo del tenant
            Tenant tenant = tenantCache.getTenant(tenantCode);
            if (tenant == null) {
                log.warn("Tenant no encontrado: {}", tenantCode);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Tenant not found: " + tenantCode);
                return;
            }

            // Bloquear requests de tenants desactivados — ISO 27001 A.9
            if (Boolean.FALSE.equals(tenant.getActive())) {
                log.warn("Acceso bloqueado: tenant [{}] está desactivado", tenantCode);
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Tenant account is disabled.");
                return;
            }

            Long tenantId = tenant.getId();

            TenantContext.setTenantId(tenantId);
            TenantContext.setTenantCode(tenantCode);
            request.setAttribute("tenantCode", tenantCode);
            request.setAttribute("tenantId", tenantId);
            MDC.put("tenantCode", tenantCode);

            log.debug("Tenant context: code={}, id={} — {}", tenantCode, tenantId, request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
            MDC.remove("tenantCode");
        }
    }

    /**
     * Extrae el tenantCode desde el subdominio del host en producción.
     * En entornos locales usa el header X-Tenant-Code como fallback.
     */
    private String extractTenantCode(HttpServletRequest request) {
        String host = request.getServerName();

        if (isLocalEnvironment(host)) {
            String headerCode = request.getHeader("X-Tenant-Code");
            log.debug("Entorno local (host={}). Usando header X-Tenant-Code: {}", host, headerCode);
            return headerCode;
        }

        int firstDot = host.indexOf('.');
        if (firstDot > 0) {
            String subdomain = host.substring(0, firstDot);
            log.debug("Subdominio extraído: {} (host: {})", subdomain, host);
            return subdomain;
        }

        log.warn("No se pudo extraer subdominio del host: {}", host);
        return null;
    }

    private boolean isLocalEnvironment(String host) {
        return host.equals("localhost")
                || host.equals("127.0.0.1")
                || host.startsWith("192.168.")
                || host.startsWith("10.")
                || host.startsWith("172.");
    }
}
