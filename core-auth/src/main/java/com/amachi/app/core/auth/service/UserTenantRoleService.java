package com.amachi.app.core.auth.service;

import com.amachi.app.core.auth.dto.UserTenantRoleDto;
import com.amachi.app.core.auth.dto.search.UserTenantRoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.common.service.GenericService;

import java.util.List;
import java.util.Set;

// TODO para borrar cuando se migre todo a UserTenantRoleServiceImpl
public interface UserTenantRoleService extends
        GenericService<UserTenantRole, UserTenantRoleDto, UserTenantRoleSearchDto> {
    // void assignRolesToUser(Long userTenantId, List<String> roles);

    void assignRolesToUserAndTenant(Long userId, Long tenantId, List<String> roleNames);

    /**
     * Asigna las roles (entidades Role) al user en el tenant y persiste
     * UserTenantRole.
     * Evita duplicados y devuelve la lista creada.
     */
    Set<UserTenantRole> assignRolesToUserAndTenant(User user, Long tenantId, Set<Role> roles);

    Set<String> findRoleNamesByUserAndTenant(Long userId, Long tenantId);

    /**
     * Desasigna (elimina) los roles especificados de un usuario en un tenant.
     */
    void unassignRolesFromUserAndTenant(Long userId, Long tenantId, List<String> roleNames);

    /**
     * Revoca un rol asignado a un usuario dentro de un tenant.
     * Marca la entidad como inactiva y registra la fecha de revocación.
     */
    void revokeRole(Long userId, Long tenantId, String roleName);
}
