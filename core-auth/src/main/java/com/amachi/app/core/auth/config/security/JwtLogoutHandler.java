package com.amachi.app.core.auth.config.security;

import com.amachi.app.core.auth.service.TokenService;
import com.amachi.app.core.common.context.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(JwtLogoutHandler.class);

    private final TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // 1️⃣ Extraer y invalidar el JWT del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            tokenService.invalidateToken(jwt);

            // El tenantCode ya está en TenantContext — establecido por MultiTenantFilter desde el subdominio
            if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User principal) {
                log.info("🚪 Logout: usuario='{}', tenant='{}'", principal.getUsername(), TenantContext.getTenantCode());
            }
        }

        // 2️⃣ Limpiar contexto de seguridad
        if (authentication != null) {
            authentication.setAuthenticated(false);
        }
    }
}
