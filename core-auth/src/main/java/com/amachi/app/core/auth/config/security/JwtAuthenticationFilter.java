package com.amachi.app.core.auth.config.security;

import com.amachi.app.core.auth.config.multiTenant.TenantCache;
import com.amachi.app.core.auth.context.AuthContext;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.exception.AppSecurityException;
import com.amachi.app.core.auth.service.AuthValidationService;
import com.amachi.app.core.auth.service.JwtService;
import com.amachi.app.core.auth.service.TokenService;
import com.amachi.app.core.auth.service.impl.JwtServiceImpl;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.api.ApiResponse;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.error.ErrorCode;
import com.amachi.app.core.common.error.ErrorDetail;
import com.amachi.app.core.common.http.HttpStatusCode;
import com.amachi.app.core.common.i18n.Translator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final TenantCache tenantCache;
        private final UserDetailsService userDetailsService;
        private final TokenService tokenService;
        private final AuthValidationService authValidationService;

        // Endpoints que no requieren validación de token (Públicos)
        // Deben coincidir con SecurityConfig.java
        private static final List<String> GLOBAL_ENDPOINTS = List.of(
                        "/auth/",
                        "/account/activate",
                        "/account/request-reset-password",
                        "/account/reset-password",
                        "/public/",
                        "/v3/api-docs",
                        "/swagger-ui",
                        "/tenants",
                        "/themes/tenant/");

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                String servletPath = request.getServletPath();
                String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

                // Reset security context only — TenantContext must NOT be cleared here
                // because MultiTenantFilter (which runs before this filter) already set it.
                // TenantContext is cleared in the finally block below.
                SecurityContextHolder.clearContext();

                boolean isGlobalEndpoint = GLOBAL_ENDPOINTS.stream().anyMatch(servletPath::startsWith);

                try {

                        // 🔹 1. Endpoint protegido sin token
                        if ((authHeader == null || !authHeader.startsWith("Bearer ")) && !isGlobalEndpoint) {
                                writeErrorResponse(response,
                                                ErrorCode.SEC_AUTHENTICATION_ERROR,
                                                Translator.toLocale("security.auth.missing.token", null),
                                                servletPath);
                                return;
                        }

                        // 🔹 2. Endpoint público sin token
                        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                                filterChain.doFilter(request, response);
                                return;
                        }

                        String token = authHeader.substring(7);

                        // 🔹 3. Validación JWT (firma + expiración)
                        jwtService.validateToken(token);

                        // 🔹 4. Blacklist
                        if (tokenService.isTokenBlacklisted(token)) {
                                throw new AppSecurityException(
                                                ErrorCode.SEC_INVALID_TOKEN,
                                                "security.token.blacklisted");
                        }

                        // 🔹 5. Extraer datos
                        String username = jwtService.extractUsername(token);
                        String tenantCode = jwtService.extractTenantCode(token);
                        List<String> roles = jwtService.extractRoles(token);

                        if (tenantCode == null || tenantCode.isBlank()) {
                                throw new AppSecurityException(
                                                ErrorCode.SEC_INVALID_TOKEN,
                                                "security.token.invalid.tenant");
                        }

                        boolean isSuperAdmin = roles.contains("ROLE_SUPER_ADMIN");

                        // 🔹 6. Resolver tenant (TU IMPLEMENTACIÓN)
                        Tenant tenant = tenantCache.getTenant(tenantCode);
                        if (tenant == null) {
                                throw new AppSecurityException(
                                                ErrorCode.SEC_TENANT_NOT_FOUND,
                                                "security.tenant.not_found",
                                                tenantCode);
                        }

                        // 🔹 7. Anti-Spoofing (TU LÓGICA ORIGINAL, CORRECTA)
                        if (!isGlobalEndpoint && !isSuperAdmin) {
                                String requestTenantCode = (String) request.getAttribute("tenantCode");

                                if (requestTenantCode != null
                                                && !requestTenantCode.equals(tenantCode)
                                                && !"GLOBAL".equalsIgnoreCase(requestTenantCode)) {

                                        log.error("🚨 SPOOFING: Token tenant '{}' vs Request tenant '{}'",
                                                        tenantCode, requestTenantCode);

                                        throw new AppSecurityException(
                                                        ErrorCode.SEC_INVALID_TOKEN,
                                                        "security.token.tenant.mismatch");
                                }
                        }

                        // 🔹 8. Cargar usuario
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (!(userDetails instanceof User user)) {
                                throw new AppSecurityException(
                                                ErrorCode.SEC_INVALID_TOKEN,
                                                "security.user.invalid");
                        }

                        // 🔹 9. Validación User-Tenant
                        if (!isSuperAdmin) {
                                authValidationService.validateUserTenant(user.getId(), tenant.getId());
                        }

                        // 🔹 10. Authorities
                        List<GrantedAuthority> authorities = roles.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toUnmodifiableList());

                        // 🔹 11. AuthContext
                        AuthContext authContext = AuthContext.builder()
                                        .userId(user.getId())
                                        .tenantId(tenant.getId())
                                        .tenantCode(tenantCode)
                                        .roles(new HashSet<>(roles))
                                        .build();

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        authContext, null, authorities);

                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 🔹 12. TenantContext (CRÍTICO)
                        TenantContext.setTenantId(tenant.getId());
                        TenantContext.setTenantCode(tenantCode);

                        log.debug("✅ Auth OK user='{}', tenant='{}', path='{}'",
                                        username, tenantCode, servletPath);

                        // 🔹 13. Continuar
                        filterChain.doFilter(request, response);

                } catch (JwtServiceImpl.TokenException ex) {

                        writeErrorResponse(response,
                                        ErrorCode.SEC_INVALID_TOKEN,
                                        Translator.toLocale("security.invalid.token", new Object[] { ex.getMessage() }),
                                        servletPath);

                } catch (AppSecurityException ex) {

                        writeErrorResponse(response,
                                        ex.getErrorCode(),
                                        Translator.toLocale(ex.getKey(), null),
                                        servletPath);

                } catch (Exception ex) {

                        log.error("🚫 Auth error: {}", ex.getMessage());

                        writeErrorResponse(response,
                                        ErrorCode.SEC_AUTHENTICATION_ERROR,
                                        Translator.toLocale("security.auth.failed", new Object[] { ex.getMessage() }),
                                        servletPath);

                } finally {

                        // 🔹 LIMPIEZA FINAL
                        TenantContext.clear();
                        SecurityContextHolder.clearContext();
                }
        }

        private void writeErrorResponse(HttpServletResponse response,
                        ErrorCode errorCode,
                        String userMessage,
                        String path) throws IOException {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");

                ErrorDetail errorDetail = ErrorDetail.from(
                                errorCode,
                                userMessage,
                                null,
                                Map.of("path", path));

                ApiResponse<Object> apiResponse = ApiResponse.error(
                                HttpStatusCode.UNAUTHORIZED,
                                errorDetail,
                                path);

                new ObjectMapper().findAndRegisterModules().writeValue(response.getWriter(), apiResponse);
        }
}
