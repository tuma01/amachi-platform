package com.amachi.app.core.auth.config.security;

import com.amachi.app.core.auth.config.multiTenant.MultiTenantFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final MultiTenantFilter multiTenantFilter;
        private final RateLimitFilter rateLimitFilter;
        private final UserDetailsService userDetailsService;
        private final PasswordEncoder passwordEncoder;
        private final LogoutHandler logoutHandler;
        private final AuthenticationProvider authenticationProvider;

        @Value("${spring.profiles.active:dev}")
        private String activeProfile;

        /**
         * ✅ Configuración principal del filtro de seguridad
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // 🔹 Desactivar CSRF para APIs JWT
                                .csrf(AbstractHttpConfigurer::disable)
                                // 🔹 Autorizaciones por endpoint — bloque unificado
                                .authorizeHttpRequests(auth -> {
                                        // Endpoints siempre públicos
                                        auth.requestMatchers(
                                                        "/auth/**",
                                                        "/account/activate",
                                                        "/account/request-reset-password",
                                                        "/account/reset-password",
                                                        "/tenants/**",
                                                        "/themes/tenant/**",
                                                        "/public/**")
                                                .permitAll();

                                        // Swagger: libre en dev, requiere ADMIN en producción — ISO 27001 A.13.2
                                        if ("dev".equalsIgnoreCase(activeProfile)) {
                                                auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll();
                                        } else {
                                                auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**")
                                                        .hasAnyRole("ADMIN", "SUPER_ADMIN");
                                        }

                                        // --- 🔐 ENDPOINTS PROTEGIDOS POR ROL ---
                                        auth.requestMatchers("/super-admin/tenants/**",
                                                        "/super-admin/tenant-admins/**")
                                                .hasRole("SUPER_ADMIN");

                                        auth.requestMatchers(HttpMethod.GET, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .authenticated();

                                        auth.requestMatchers(HttpMethod.POST, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN");

                                        auth.requestMatchers(HttpMethod.PUT, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN");

                                        auth.requestMatchers(HttpMethod.DELETE, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN");

                                        auth.requestMatchers("/employee/**").hasAnyRole("ADMIN", "TENANT_ADMIN");
                                        auth.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "TENANT_ADMIN", "SUPER_ADMIN");
                                        auth.requestMatchers(HttpMethod.GET, "/doctor/**").hasAnyRole("DOCTOR", "ADMIN", "TENANT_ADMIN");
                                        auth.requestMatchers(HttpMethod.POST, "/doctor/**").hasAnyRole("DOCTOR", "TENANT_ADMIN");
                                        auth.requestMatchers("/nurse/**").hasAnyRole("NURSE", "DOCTOR", "ADMIN", "TENANT_ADMIN");
                                        auth.requestMatchers("/patient/**").hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN", "TENANT_ADMIN");
                                        auth.requestMatchers("/tenantConfigs/**").hasAnyRole("ADMIN", "TENANT_ADMIN", "SUPER_ADMIN");

                                        // --- 🔒 CUALQUIER OTRO ENDPOINT REQUIERE AUTENTICACIÓN ---
                                        auth.anyRequest().authenticated();
                                })
                                // 🔹 Política de sesión sin estado (JWT)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // 🔹 Provider personalizado
                                .authenticationProvider(authenticationProvider)

                                .addFilterBefore(rateLimitFilter,    UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(multiTenantFilter,  UsernamePasswordAuthenticationFilter.class)
                                .addFilterBefore(jwtAuthFilter,      UsernamePasswordAuthenticationFilter.class);

                // 🔹 Configuraciones especiales para entorno dev
                if ("dev".equalsIgnoreCase(activeProfile)) {
                        http.cors(cors -> cors.configurationSource(corsConfigurationSource())); // Habilitar CORS en dev
                }

                return http.build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // Producción: subdominios de vitalia.com
                // Desarrollo: localhost con cualquier puerto y subdominios *.localhost (simulación local)
                // X-Tenant-Code se mantiene como header permitido únicamente para dev (fallback del filter)
                configuration.setAllowedOriginPatterns(List.of(
                                "https://*.vitalia.com",
                                "http://localhost:*",
                                "http://*.localhost:*"));

                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Tenant-Code"));
                configuration.setExposedHeaders(List.of("Authorization", "Content-Disposition"));
                configuration.setAllowCredentials(true);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}

