package com.amachi.app.core.auth.config.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Rate limiting para endpoints de autenticación — ISO 27001 A.9.4
 *
 * Protege contra ataques de fuerza bruta en /auth/login y /auth/refresh.
 * Límite: 10 intentos por IP por minuto. Al superarlo devuelve HTTP 429.
 *
 * Implementación: Caffeine cache (IP → contador) con TTL de 1 minuto.
 * El contador se reinicia automáticamente al expirar la ventana.
 */
@Component
@Slf4j
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    private static final List<String> RATE_LIMITED_PATHS = List.of(
            "/auth/login",
            "/auth/refresh"
    );

    private final Cache<String, AtomicInteger> ipRequestCount = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return RATE_LIMITED_PATHS.stream().noneMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException, ServletException {
        String ip = resolveClientIp(request);
        AtomicInteger count = ipRequestCount.get(ip, k -> new AtomicInteger(0));

        int current = count.incrementAndGet();

        if (current > MAX_REQUESTS_PER_MINUTE) {
            log.warn("Rate limit superado: IP={} intentos={} path={}", ip, current, request.getServletPath());
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"status\":429,\"error\":\"TOO_MANY_REQUESTS\"," +
                    "\"message\":\"Demasiados intentos de acceso. Espere 1 minuto antes de reintentar.\"}"
            );
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * Resuelve la IP real del cliente.
     * En entornos con proxy/load balancer, usa X-Forwarded-For.
     */
    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
