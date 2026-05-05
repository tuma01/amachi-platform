package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTenantRoleRepository
                extends TenantCommonRepository<UserTenantRole, Long>, UserTenantRoleRepositoryCustom {
//assignRolesToUserAndTenant
        @Query("SELECT utr.role.name FROM UserTenantRole utr WHERE utr.user = :user AND utr.tenant.code = :tenantCode AND utr.active = true AND utr.revokedAt IS NULL")
        List<String> findActiveRolesByUserAndTenantCode(@Param("user") User user,
                        @Param("tenantCode") String tenantCode);

        // Nuevo método para buscar roles activos por usuario (sin filtrar por tenant)
        @Query("SELECT utr.role.name FROM UserTenantRole utr WHERE utr.user = :user AND utr.active = true AND utr.revokedAt IS NULL")
        List<String> findActiveRolesByUser(@Param("user") User user);

        @Query("SELECT utr.role.name FROM UserTenantRole utr " +
                        "WHERE utr.user = :user AND utr.tenant = :tenant AND utr.active = true AND utr.revokedAt IS NULL")
        List<String> findActiveRolesByUserAndTenant(@Param("user") User user, @Param("tenant") Tenant tenant);

        // Método adicional para verificar directamente si es SUPER_ADMIN
        default boolean isUserSuperAdmin(User user) {
                return findActiveRolesByUser(user).stream()
                                .anyMatch(r -> r.equalsIgnoreCase("SUPER_ADMIN")
                                                || r.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
        }

        boolean existsByUserAndTenantAndRole(User user, Tenant tenant, Role role);

        // Find UserTenantRole entries for a user, tenant and a list of roles (active
        // only)
        List<UserTenantRole> findByUserAndTenantAndRoleIn(User user, Tenant tenant, List<Role> roles);

        @Query("""
                            SELECT r.name
                            FROM UserTenantRole utr
                            JOIN utr.role r
                            WHERE utr.user.id = :userId
                              AND utr.tenant.id = :tenantId
                              AND utr.active = true
                        """)
        List<String> findActiveRoleNamesByUserAndTenant(@Param("userId") Long userId,
                        @Param("tenantId") Long tenantId);

        Optional<UserTenantRole> findFirstByTenantIdAndRoleId(Long tenantId, Long roleId);

        /**
         * Recupera los nombres de los permisos asociados a los roles activos del
         * usuario en el tenant.
         * Realiza un JOIN explícito entre UserTenantRole -> Role -> RolePermission ->
         * Permission.
         */
        @Query("""
                            SELECT DISTINCT p.name
                            FROM UserTenantRole utr
                            JOIN utr.role r
                            JOIN RolePermission rp ON rp.role = r
                            JOIN rp.permission p
                            WHERE utr.user = :user
                              AND utr.tenant = :tenant
                              AND utr.active = true
                              AND utr.revokedAt IS NULL
                        """)
        List<String> findActivePermissionsNamesByUserAndTenant(@Param("user") User user,
                        @Param("tenant") Tenant tenant);

        boolean existsByUserAndTenantIdAndRoleAndDeletedFalse(User user, Long tenantId, Role role);
}
