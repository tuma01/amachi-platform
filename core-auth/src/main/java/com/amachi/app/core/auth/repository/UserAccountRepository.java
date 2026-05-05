package com.amachi.app.core.auth.repository;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositorio para la entidad UserAccount.
 * Es crucial en la arquitectura Multi-Tenant ya que vincula a User, Person,
 * Tenant y Roles.
 * Ubicado en amachi-platform.
 */
@Repository
public interface UserAccountRepository extends TenantCommonRepository<UserAccount, Long> {

    /**
     * Busca una UserAccount por el email del usuario y el código del tenant.
     * Es la consulta principal utilizada durante el proceso de autenticación.
     * 
     * @param email      Email del usuario.
     * @param tenantCode Código del tenant (ej: "HSP001").
     * @return Optional de UserAccount.
     */
    @Query("""
                SELECT ua
                FROM UserAccount ua
                JOIN FETCH ua.user u
                JOIN FETCH ua.tenant t
                WHERE u.email = :email
                AND t.code = :tenantCode
            """)
    Optional<UserAccount> findByUserEmailAndTenantCode(
            @Param("email") String email,
            @Param("tenantCode") String tenantCode);

    /**
     * Busca la UserAccount activa basada en el usuario y el tenant.
     * Es crucial para establecer el contexto de sesión después del login (ej.
     * obtener el perfil).
     * 
     * @param user   el usuario (desde JWT/security context).
     * @param tenant el tenant (desde TenantContext).
     * @return Optional con la UserAccount.
     */
    Optional<UserAccount> findByUserAndTenant(User user, Tenant tenant);

    /**
     * Verifica si existe una relación entre un Usuario y un Tenant, útil para la
     * gestión de acceso.
     * 
     * @param user   El usuario.
     * @param tenant El tenant.
     * @return true si la cuenta de usuario existe.
     */
    boolean existsByUserAndTenant(User user, Tenant tenant);

    /**
     * Busca todas las cuentas asociadas a un usuario, útil para la selección de
     * Tenant después del login.
     * 
     * @param user el usuario.
     * @return Conjunto de UserAccount.
     */
    Set<UserAccount> findByUser(User user);

    Optional<UserAccount> findByUserAndTenantCode(User user, String tenantCode);

    Optional<UserAccount> findByPerson(Person person);

    // Encuentra todos los UserAccount de un tenant específico
    List<UserAccount> findByTenant(Tenant tenant);

    // Opcional: filtrar por rol si quieres precisión
    // @Query("SELECT ua FROM UserAccount ua JOIN ua.roles r WHERE ua.tenant.id =
    // :tenantId AND r.name = :roleName")
    // List<UserAccount> findByTenantIdAndRoleName(@Param("tenantId") Long tenantId,
    // @Param("roleName") String roleName);
}
