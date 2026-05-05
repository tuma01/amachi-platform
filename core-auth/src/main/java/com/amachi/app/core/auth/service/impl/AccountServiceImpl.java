package com.amachi.app.core.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.auth.bridge.TenantBridge;
import com.amachi.app.core.auth.dto.ActivationRequest;
import com.amachi.app.core.auth.dto.ChangePasswordRequest;
import com.amachi.app.core.auth.dto.JwtUserDto;
import com.amachi.app.core.auth.dto.PasswordResetConfirmationRequest;
import com.amachi.app.core.auth.dto.PasswordResetRequest;
import com.amachi.app.core.auth.entity.PasswordResetToken;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.auth.exception.AppSecurityException;
import com.amachi.app.core.auth.repository.PasswordResetTokenRepository;
import com.amachi.app.core.auth.repository.UserAccountRepository;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.auth.repository.UserTenantRoleRepository;
import com.amachi.app.core.auth.service.AccountService;
import com.amachi.app.core.auth.service.JwtService;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.dto.UserSummaryDto;
import com.amachi.app.core.common.error.ErrorCode;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * Módulo: vitalia-security
 *
 * Implementación de la lógica de gestión de cuentas (activación, perfil,
 * contraseñas).
 * (SaaS Elite Identity Hardened - Manual implementation for reliability)
 */
@Service
@TenantAware
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private static final Duration RESET_TOKEN_VALIDITY = Duration.ofHours(24);

    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;
    private final TenantBridge tenantBridge;
    private final UserTenantRoleRepository userTenantRoleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void activateAccount(ActivationRequest request) {
        JwtUserDto userDto = jwtService.extractUserDto(request.getToken());
        User user = userRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND, "error.resource.not.found", userDto.getUserId().toString()));

        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Account activated for user: {}", user.getEmail());
    }

    @Transactional
    public void requestPasswordReset(PasswordResetRequest request) {
        String tenantCode = TenantContext.getTenantCode();
        tenantBridge.findByCode(tenantCode);

        User user = userAccountRepository
                .findByUserEmailAndTenantCode(request.getEmail(), tenantCode)
                .map(UserAccount::getUser)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND,
                        "security.user.not_found_in_tenant", request.getEmail()));

        passwordResetTokenRepository.deleteByUser(user);

        JwtUserDto jwtUserDto = JwtUserDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .tenantCode(tenantCode)
                .build();

        String resetToken = jwtService.generatePasswordResetToken(jwtUserDto);

        PasswordResetToken tokenEntity = PasswordResetToken.builder()
                .token(resetToken)
                .user(user)
                .expirationDate(Instant.now().plus(RESET_TOKEN_VALIDITY))
                .build();

        passwordResetTokenRepository.save(tokenEntity);
        log.info("📩 Password reset token generated for {} in tenant {}", user.getEmail(), tenantCode);
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetConfirmationRequest request) {
        log.info("🔄 Restableciendo contraseña para token");

        PasswordResetToken tokenEntity = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_INVALID_TOKEN, "security.invitation.not_found"));

        if (tokenEntity.isInvalid() || !jwtService.isPasswordResetToken(tokenEntity.getToken())) {
            throw new AppSecurityException(ErrorCode.SEC_INVALID_TOKEN, "security.invitation.expired");
        }
        User user = tokenEntity.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenEntity.setUsed(true);
        passwordResetTokenRepository.save(tokenEntity);

        log.info("✅ Contraseña restablecida correctamente para usuario: {}", user.getEmail());
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND, "error.resource.not.found", userId.toString()));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppSecurityException(ErrorCode.SEC_INVALID_CREDENTIALS, "security.password.incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", user.getEmail());
    }

    @Transactional
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND, "error.resource.not.found", userId.toString()));

        user.setEnabled(false);
        userRepository.save(user);
        log.warn("⛔ Account deactivated (soft delete) for user ID: {}", userId);
    }

    @Transactional(readOnly = true)
    public UserSummaryDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_USER_NOT_FOUND, "error.resource.not.found", userId.toString()));

        Long currentTenantId = TenantContext.getTenantId();
        Tenant tenant = (currentTenantId != null) ? tenantBridge.findById(currentTenantId) : null;

        String tenantCode = tenant != null ? tenant.getCode() : null;
        String tenantName = tenant != null ? tenant.getName() : null;
        Long tenantId = tenant != null ? tenant.getId() : null;

        List<String> roles = userTenantRoleRepository.findActiveRolesByUserAndTenantCode(user, tenantCode);

        // Resolve RoleContext via Bridge (Hardened Elite modularity)
        RoleContext activeRoleContext = null;
        if (tenantCode != null && user.getPerson() != null) {
            activeRoleContext = tenantBridge.findRoleContext(user.getPerson().getId(), tenantCode);
        }

        return UserSummaryDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .personName(user.getPerson() != null ? user.getPerson().getFullName() : null)
                .roleContext(activeRoleContext)
                .tenantId(tenantId)
                .tenantCode(tenantCode)
                .tenantName(tenantName)
                .roles(roles)
                .build();
    }
}
