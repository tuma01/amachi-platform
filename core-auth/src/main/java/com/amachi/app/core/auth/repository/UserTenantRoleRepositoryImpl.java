package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Custom implementation for UserTenantRole repository operations.
 */
public class UserTenantRoleRepositoryImpl implements UserTenantRoleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void assignRolesToUserAndTenant(Long userId, String tenantCode, Set<String> roleNames) {
        // 1. Resolve User
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // 2. Resolve Tenant
        Tenant tenant = entityManager.createQuery(
                "SELECT t FROM Tenant t WHERE t.code = :code", Tenant.class)
                .setParameter("code", tenantCode)
                .getSingleResult();

        // 3. Assign each role
        for (String roleName : roleNames) {
            Role role = entityManager.createQuery(
                    "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", roleName)
                    .getSingleResult();

            // 4. Create assignment if not exists
            Long exists = entityManager.createQuery(
                    "SELECT COUNT(utr) FROM UserTenantRole utr WHERE utr.user = :user AND utr.tenant = :tenant AND utr.role = :role", Long.class)
                    .setParameter("user", user)
                    .setParameter("tenant", tenant)
                    .setParameter("role", role)
                    .getSingleResult();

            if (exists == 0) {
                UserTenantRole utr = UserTenantRole.builder()
                        .user(user)
                        .tenantId(tenant.getId())
                        .tenantCode(tenantCode)
                        .role(role)
                        .assignedAt(LocalDateTime.now())
                        .active(true)
                        .build();
                entityManager.persist(utr);
            }
        }
    }
}
