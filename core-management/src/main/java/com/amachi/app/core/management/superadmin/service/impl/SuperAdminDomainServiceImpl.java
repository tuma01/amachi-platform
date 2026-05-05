package com.amachi.app.core.management.superadmin.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.common.config.AppBootstrapProperties;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import com.amachi.app.core.management.superadmin.entity.SuperAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.requireNonNull;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SuperAdminDomainServiceImpl {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppBootstrapProperties appBootstrapProperties;
    private final UserProvisioningService userProvisioningService;

    /**
     * Professional setup of the UserAccount and Roles after the SuperAdmin is
     * persisted.
     * Use "Split Persistence" strategy to handle the personId constraint.
     */
    public void completeAccountSetup(SuperAdmin savedEntity) {
        // 1. Validation
        if (savedEntity.getId() == null) {
            throw new IllegalStateException("SuperAdmin must be saved before setting up account.");
        }
        requireNonNull(savedEntity.getPerson(), "Person is required");
        requireNonNull(savedEntity.getUser(), "User is required");

        // 2. Find Global Tenant
        String globalTenantCode = appBootstrapProperties.getTenant().getTenantGlobal().getCode();
        Tenant globalTenant = tenantRepository.findByCode(globalTenantCode)
                .orElseThrow(() -> new IllegalStateException("Global Tenant not found with code: " + globalTenantCode));

        // 3. Delegate to Provisioning Service (Idempotent)
        userProvisioningService.provision(ProvisioningRequest.builder()
                .email(savedEntity.getUser().getEmail())
                .tenant(globalTenant)
                .roleContext(RoleContext.SUPER_ADMIN)
                .domainContext(DomainContext.SUPER_ADMIN)
                .resolvedPerson(savedEntity.getPerson()) // 🟢 Usamos la entidad ya existente
                .resolvedUser(savedEntity.getUser())     // 🟢 Usamos la entidad ya existente
                .build());

        log.info("✔ Account setup (Provisioning) completed for SuperAdmin ID: {}", savedEntity.getId());
    }

    // Overloaded to match controller usage directly if passing entity
    public void encodePasswordIfNeeded(SuperAdmin entity) {
        User user = entity.getUser();
        if (user == null) {
            return;
        }
        if (user.getId() == null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    }
}
