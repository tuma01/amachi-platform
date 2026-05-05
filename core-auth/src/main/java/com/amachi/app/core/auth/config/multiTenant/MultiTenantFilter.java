package com.amachi.app.core.auth.config.multiTenant;

import com.amachi.app.core.common.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * Los flujos de auth (/auth/**, /account/**) ya NO están aquí — el subdominio
     * provee el tenant incluso para peticiones no autenticadas.
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

    public MultiTenantFilter(@Lazy TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        boolean skip = NO_TENANT_PATHS.stream()
                .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
        if (skip) {
            log.debug("⏭️ MultiTenantFilter skipped for: {}", path);
        }
        return skip;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1️⃣ Extraer tenantCode desde el subdominio (fuente de verdad en producción)
            //    Fallback a header X-Tenant-Code en entornos locales de desarrollo
            String tenantCode = extractTenantCode(request);

            if (tenantCode == null || tenantCode.isBlank()) {
                log.warn("🚨 Tenant no resuelto para: {}", request.getRequestURI());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Tenant could not be resolved. Ensure you are using a valid subdomain.");
                return;
            }

            // 2️⃣ Resolver TenantId (Long) con caché
            Long tenantId = tenantResolver.resolveTenantId(tenantCode);

            // 3️⃣ Establecer contexto de tenant para aislamiento de BD
            TenantContext.setTenantId(tenantId);
            TenantContext.setTenantCode(tenantCode);

            // 4️⃣ Propagar como atributos del request para filtros posteriores
            request.setAttribute("tenantCode", tenantCode);
            request.setAttribute("tenantId", tenantId);

            log.debug("✅ Tenant context: code={}, id={} — {}", tenantCode, tenantId, request.getRequestURI());

            filterChain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }

    /**
     * Extrae el tenantCode desde el subdominio del host.
     * Ejemplo: "hospital-san-borja.vitalia.com" → "hospital-san-borja"
     *
     * En entornos locales (localhost, 127.0.0.1, redes privadas) usa el header
     * X-Tenant-Code como fallback para facilitar el desarrollo y las pruebas.
     */
    private String extractTenantCode(HttpServletRequest request) {
        String host = request.getServerName();

        if (isLocalEnvironment(host)) {
            String headerCode = request.getHeader("X-Tenant-Code");
            log.debug("🛠️ Entorno local (host={}). Usando header X-Tenant-Code: {}", host, headerCode);
            return headerCode;
        }

        // Producción: primer segmento del host es el subdominio
        int firstDot = host.indexOf('.');
        if (firstDot > 0) {
            String subdomain = host.substring(0, firstDot);
            log.debug("🌐 Subdominio extraído: {} (host: {})", subdomain, host);
            return subdomain;
        }

        log.warn("⚠️ No se pudo extraer subdominio del host: {}", host);
        return null;
    }

    /**
     * Detecta entornos de desarrollo local por el hostname.
     * Incluye localhost, IPs de loopback y rangos de red privada (RFC 1918).
     */
    private boolean isLocalEnvironment(String host) {
        return host.equals("localhost")
                || host.equals("127.0.0.1")
                || host.startsWith("192.168.")
                || host.startsWith("10.")
                || host.startsWith("172.");
    }
}
