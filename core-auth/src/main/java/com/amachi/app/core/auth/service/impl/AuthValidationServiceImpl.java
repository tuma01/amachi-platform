package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.auth.service.AuthValidationService;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.common.error.ErrorCode;
import com.amachi.app.core.auth.exception.AppSecurityException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthValidationServiceImpl implements AuthValidationService {

    private static final Logger log = LoggerFactory.getLogger(AuthValidationServiceImpl.class);

    private final UserRepository userRepository;
    private final PersonTenantRepository personTenantRepository;

    @Override
    @Transactional(readOnly = true)
    public void validateUserTenant(Long userId, Long tenantId) {
        log.trace("🛡️ Validating User-Tenant relationship: User [{}], Tenant [{}]", userId, tenantId);

        // 1. Obtener el usuario y su persona asociada
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppSecurityException(ErrorCode.SEC_AUTHENTICATION_ERROR, "security.user.not_found"));

        if (user.getPerson() == null) {
            log.error("🚨 User [{}] has no associated Person entity", userId);
            throw new AppSecurityException(ErrorCode.SEC_AUTHENTICATION_ERROR, "security.user.no_person");
        }

        Long personId = user.getPerson().getId();

        // 2. Verificar si existe la relación en DMN_PERSON_TENANT
        boolean hasAccess = personTenantRepository.findByPersonIdAndTenantId(personId, tenantId).isPresent();

        if (!hasAccess) {
            log.warn("⛔ User [{}] (Person [{}]) does NOT belong to Tenant [{}]", userId, personId, tenantId);
            throw new AppSecurityException(ErrorCode.SEC_INVALID_TENANT, "security.tenant.access_denied");
        }

        log.trace("✅ User-Tenant validation successful");
    }
}
