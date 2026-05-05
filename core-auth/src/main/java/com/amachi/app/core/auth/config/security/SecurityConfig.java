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
        private final MultiTenantFilter multiTenantFilter; // Injected
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
                                // 🔹 Autorizaciones por endpoint
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/auth/**", // login, register, refresh
                                                                "/account/activate", // Public activation
                                                                "/account/request-reset-password", // Public reset request
                                                                "/account/reset-password", // Public reset confirmation
                                                                "/tenants/**", // Permitir listar tenants públicamente para login
                                                                "/themes/tenant/**", // Public branding access
                                                                "/public/**", // healthcheck o documentación
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**")
                                                .permitAll()

                                                // --- 🔐 ENDPOINTS PROTEGIDOS POR ROL ---
                                                .requestMatchers("/super-admin/tenants/**",
                                                                "/super-admin/tenant-admins/**")
                                                .hasRole("SUPER_ADMIN")

                                                .requestMatchers(HttpMethod.GET, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .authenticated()

                                                .requestMatchers(HttpMethod.POST, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN")

                                                .requestMatchers(HttpMethod.PUT, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN")

                                                .requestMatchers(HttpMethod.DELETE, "/countries/**", "/states/**", "/provinces/**",
                                                        "/municipalities/**", "/addresses/**")
                                                .hasRole("SUPER_ADMIN")

                                                .requestMatchers("/employee/**").hasRole("ADMIN")
                                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
                                                .requestMatchers(HttpMethod.GET, "/doctor/**")
                                                .hasAnyRole("DOCTOR", "ADMIN")
                                                .requestMatchers(HttpMethod.POST, "/doctor/**").hasRole("DOCTOR")

                                                .requestMatchers("/nurse/**").hasAnyRole("NURSE", "DOCTOR", "ADMIN")
                                                .requestMatchers("/patient/**")
                                                .hasAnyRole("PATIENT", "DOCTOR", "NURSE", "ADMIN")

                                                .requestMatchers("/tenantConfigs/**")
                                                .hasAnyRole("ADMIN", "TENANT_ADMIN", "SUPER_ADMIN")

                                                // --- 🔒 CUALQUIER OTRO ENDPOINT REQUIERE AUTENTICACIÓN ---
                                                .anyRequest().authenticated())
                                // 🔹 Política de sesión sin estado (JWT)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // 🔹 Provider personalizado
                                .authenticationProvider(authenticationProvider)

                                .addFilterBefore(multiTenantFilter, UsernamePasswordAuthenticationFilter.class)
                                .addFilterAfter(jwtAuthFilter, MultiTenantFilter.class);

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

