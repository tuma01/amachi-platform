package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.AuditEventType;
import com.amachi.app.core.auth.auditevent.service.AuditService;
import com.amachi.app.core.auth.bridge.TenantBridge;
import com.amachi.app.core.auth.dto.AuthenticationRequest;
import com.amachi.app.core.auth.dto.AuthenticationResponse;
import com.amachi.app.core.auth.dto.JwtUserDto;
import com.amachi.app.core.auth.dto.UserRegisterRequest;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.auth.exception.AppSecurityException;
import com.amachi.app.core.auth.repository.UserAccountRepository;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.auth.repository.UserTenantRoleRepository;
import com.amachi.app.core.auth.service.*;
import com.amachi.app.core.common.dto.TokenPairDto;
import com.amachi.app.core.common.dto.UserSummaryDto;
import com.amachi.app.core.common.error.ErrorCode;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final AuditService auditService;
    private final AuthenticationManager authenticationManager;
    private final TenantBridge tenantBridge;
    private final PersonTenantRepository personTenantRepository;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public AuthenticationResponse register(UserRegisterRequest request) {
        log.info("📝 Registro de nuevo usuario email='{}'", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppSecurityException(ErrorCode.SEC_INVALID_OPERATION, "security.user.already_exists", request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .accountLocked(false)
                .build();

        user = userRepository.save(user);

        UserSummaryDto userSummary = UserSummaryDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .build();

        return AuthenticationResponse.builder().user(userSummary).build();
    }

    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("🔐 Autenticación iniciada para email='{}' tenant='{}'", request.getEmail(), TenantContext.getTenantCode());

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new AppSecurityException(ErrorCode.SEC_INVALID_OPERATION, "security.auth.missing_credentials");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND, "security.user.not_found", request.getEmail()));

        String effectiveTenantCode = resolveTenantForLogin(user);
        Tenant tenant = tenantBridge.findByCode(effectiveTenantCode);

        if (Boolean.FALSE.equals(tenant.getActive())) {
            throw new AppSecurityException(ErrorCode.SEC_TENANT_DISABLED, "security.tenant.disabled", tenant.getCode());
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception ex) {
            throw new AppSecurityException(ErrorCode.SEC_INVALID_CREDENTIALS, "security.auth.invalid_credentials");
        }

        UserAccount userAccount = validateTenantAccess(user, tenant);
        List<String> authorities = loadEffectiveAuthorities(userAccount);

        if (authorities.isEmpty()) {
            throw new AppSecurityException(ErrorCode.SEC_FORBIDDEN, "security.user.no_roles_in_tenant", tenant.getCode());
        }

        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities));

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return buildAuthenticationResponse(user, tenant, authorities);
    }

    private String resolveTenantForLogin(User user) {
        String tenantCode = TenantContext.getTenantCode();
        if (tenantCode != null && !tenantCode.isBlank()) {
            return tenantCode;
        }
        // El MultiTenantFilter garantiza que el contexto siempre está presente.
        // Si llegamos aquí es un fallo de configuración del filter, no de negocio.
        log.error("⚠️ TenantContext vacío en login para user={}. Verificar MultiTenantFilter.", user.getEmail());
        throw new AppSecurityException(ErrorCode.SEC_INVALID_OPERATION, "security.tenant.required_for_user");
    }

    private UserAccount validateTenantAccess(User user, Tenant tenant) {
        // 🛡️ [SENIOR-FIX] Desactivamos el filtro de tenant para permitir el "handshake" durante el login.
        // Esto evita que la 'Sombra de Hibernate' bloquee la búsqueda cuando el contexto aún es nulo.
        org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
        session.disableFilter("tenantFilter");

        // Usamos la relación real (FK) con el objeto Tenant, evitando la columna redundante tenant_code.
        UserAccount account = userAccountRepository.findByUserAndTenant(user, tenant)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_FORBIDDEN, "security.user.no_account_in_tenant", tenant.getCode()));

        if (!user.isEnabled()) throw new AppSecurityException(ErrorCode.SEC_USER_DISABLED, "security.user.disabled", user.getEmail());
        if (user.isAccountLocked()) throw new AppSecurityException(ErrorCode.SEC_USER_LOCKED, "security.user.locked", user.getEmail());

        return account;
    }

    private List<String> loadEffectiveAuthorities(UserAccount account) {
        List<String> roles = userTenantRoleRepository.findActiveRolesByUserAndTenant(account.getUser(), account.getTenant())
                .stream().map(name -> name.startsWith("ROLE_") ? name : "ROLE_" + name).collect(Collectors.toCollection(ArrayList::new));

        List<String> permissions = userTenantRoleRepository.findActivePermissionsNamesByUserAndTenant(account.getUser(), account.getTenant());
        roles.addAll(permissions);
        return roles.stream().distinct().collect(Collectors.toList());
    }

    private AuthenticationResponse buildAuthenticationResponse(User user, Tenant tenant, List<String> roles) {
        JwtUserDto jwtUser = JwtUserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .tenantCode(tenant.getCode())
                .roles(roles)
                .build();

        // 🛡️ [SECURITY-HANDSHAKE] Establecemos el contexto del tenant para permitir la persistencia 
        // de tokens (RefreshToken) y auditoría que requieren aislamiento.
        TenantContext.setTenantId(tenant.getId());
        TenantContext.setTenantCode(tenant.getCode());
        try {
            TokenPairDto tokenPair = tokenService.generateAndStoreTokenPair(jwtUser, user, tenant);

            if (auditService != null) {
                auditService.registerEvent(AuditEventType.USER_LOGIN, user.getId(), tenant.getId(), tenant.getCode(), "User logged in");
            }
            
            return AuthenticationResponse.builder()
                    .tokens(tokenPair)
                    .user(buildUserSummary(user, tenant, roles))
                    .build();
        } finally {
            com.amachi.app.core.common.context.TenantContext.clear();
        }
    }

    private UserSummaryDto buildUserSummary(User user, Tenant tenant, List<String> roles) {
        String personName = null;
        if (user.getPerson() != null) {
            personName = (user.getPerson().getFirstName() + " " + user.getPerson().getLastName()).trim();
        }

        com.amachi.app.core.common.enums.RoleContext roleContext = null;
        if (user.getPerson() != null && tenant.getCode() != null) {
            roleContext = tenantBridge.findRoleContext(user.getPerson().getId(), tenant.getCode());
        }

        return UserSummaryDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .personName(personName)
                .roleContext(roleContext)
                .enabled(user.isEnabled())
                .lastLogin(user.getLastLogin())
                .tenantId(tenant.getId())
                .tenantCode(tenant.getCode())
                .tenantName(tenant.getName())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public AuthenticationResponse refreshToken(String refreshToken) {
        log.info("🔄 Refreshing access token");
        return refreshTokenService.findByToken(refreshToken)
                .map(token -> {
                    if (!token.isValid()) {
                        throw new AppSecurityException(ErrorCode.SEC_INVALID_TOKEN, "security.refreshToken.expired");
                    }
                    User user = token.getUser();
                    Tenant tenant = tenantBridge.findByCode(token.getTenantCode());

                    UserAccount userAccount = validateTenantAccess(user, tenant);
                    List<String> authorities = loadEffectiveAuthorities(userAccount);

                    return buildAuthenticationResponse(user, tenant, authorities);
                })
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_INVALID_TOKEN, "security.refreshToken.not_found"));
    }

    @Override
    @Transactional
    public void logout(String refreshToken, String accessToken) {
        if (accessToken != null && !accessToken.isBlank()) {
            try { tokenService.invalidateToken(accessToken); }
            catch (Exception ignored) {}
        }

        refreshTokenService.findByToken(refreshToken).ifPresent(token ->
            refreshTokenService.deleteByUserIdAndTenantCode(token.getUser().getId(), token.getTenantCode()));


        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional(readOnly = true)
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
