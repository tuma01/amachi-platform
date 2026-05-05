package com.amachi.app.core.management.provisioning.service.impl;

import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.auth.identity.service.IdentityResolutionService;
import com.amachi.app.core.auth.repository.RoleRepository;
import com.amachi.app.core.auth.repository.UserAccountRepository;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.auth.service.UserTenantRoleService;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.core.domain.context.service.PersonContextService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.dto.ProvisioningResult;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProvisioningServiceImpl implements UserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAccountRepository userAccountRepository;
    private final UserTenantRoleService userTenantRoleService;
    private final IdentityResolutionService identityResolutionService;
    private final PersonContextService personContextService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ProvisioningResult provision(ProvisioningRequest request) {
        log.info("🚀 [PROVISIONING] Iniciando ciclo para email='{}' en tenant='{}' (Rol: {})", 
                request.getEmail(), request.getTenant().getCode(), request.getRoleContext());

        // 1. Resolver Identidad (Person)
        Person person = resolvePerson(request);
        log.debug("   ↳ Identidad resuelta: ID={}", person.getId());

        // 2. Resolver/Crear Usuario (User)
        User user = resolveUser(request, person);
        log.debug("   ↳ Usuario resuelto: ID={}, Email={}", user.getId(), user.getEmail());

        // 3. Crear Contexto (PersonTenant)
        personContextService.createContext(
                person, 
                request.getTenant(), 
                request.getRoleContext(), 
                request.getDomainContext()
        );
        log.debug("   ↳ Contexto PersonTenant verificado/creado");

        // 4. Crear Cuenta (UserAccount) - CRITICO PARA LOGIN
        UserAccount account = createAccountIfAbsent(user, person, request.getTenant());
        log.debug("   ↳ UserAccount verificado/creado: ID={}, Code={}", account.getId(), account.getTenantCode());

        // 5. Asignación de Roles (UserTenantRole)
        Set<UserTenantRole> roles = assignRoles(user, request);
        log.debug("   ↳ Roles asignados: {}", roles.size());

        log.info("✅ [PROVISIONING] Ciclo completado exitosamente para '{}'", request.getEmail());

        return ProvisioningResult.builder()
                .person(person)
                .user(user)
                .userAccount(account)
                .roles(roles)
                .build();
    }

    private Person resolvePerson(ProvisioningRequest request) {
        if (request.getResolvedPerson() != null) {
            return request.getResolvedPerson();
        }
        
        Person personData = Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();
        
        return identityResolutionService.resolveOrCreateIdentity(null, request.getEmail(), personData);
    }

    private User resolveUser(ProvisioningRequest request, Person person) {
        if (request.getResolvedUser() != null) {
            return request.getResolvedUser();
        }

        return userRepository.findByEmail(request.getEmail())
                .map(existingUser -> {
                    if (existingUser.getPerson() == null) {
                        existingUser.setPerson(person);
                        return userRepository.saveAndFlush(existingUser);
                    }
                    return existingUser;
                })
                .orElseGet(() -> {
                    log.info("👤 [PROVISIONING] Creando nuevo usuario: {}", request.getEmail());
                    User newUser = User.builder()
                            .email(request.getEmail())
                            .password(request.getRawPassword() != null ? 
                                    passwordEncoder.encode(request.getRawPassword()) : null)
                            .enabled(true)
                            .person(person)
                            .build();
                    
                    return userRepository.saveAndFlush(newUser);
                });
    }

    private UserAccount createAccountIfAbsent(User user, Person person, Tenant tenant) {
        return userAccountRepository.findByUserAndTenant(user, tenant)
                .map(existingAccount -> {
                    // 🔧 Reparación de datos (Self-Healing): Si el código es incorrecto (ej. "SYSTEM" legacy), lo arreglamos.
                    boolean needsRepair = false;
                    if (!tenant.getCode().equals(existingAccount.getTenantCode())) {
                        log.info("🔧 [PROVISIONING] Reparando tenantCode para '{}': '{}' -> '{}'", 
                                user.getEmail(), existingAccount.getTenantCode(), tenant.getCode());
                        existingAccount.setTenantCode(tenant.getCode());
                        needsRepair = true;
                    }
                    if (existingAccount.getTenantId() == null || existingAccount.getTenantId() == 0L) {
                        log.info("🔧 [PROVISIONING] Reparando tenantId para '{}': 0 -> {}", user.getEmail(), tenant.getId());
                        existingAccount.setTenantId(tenant.getId());
                        needsRepair = true;
                    }
                    
                    return needsRepair ? userAccountRepository.saveAndFlush(existingAccount) : existingAccount;
                })
                .orElseGet(() -> {
                    log.info("🔗 [PROVISIONING] Creando UserAccount para '{}' en '{}'", 
                            user.getEmail(), tenant.getCode());
                    UserAccount account = UserAccount.builder()
                            .user(user)
                            .person(person)
                            .tenant(tenant)
                            .tenantId(tenant.getId() != null ? tenant.getId() : 0L)
                            .tenantCode(tenant.getCode())
                            .build();
                    return userAccountRepository.saveAndFlush(account);
                });
    }

    private Set<UserTenantRole> assignRoles(User user, ProvisioningRequest request) {
        Set<Role> rolesToAssign = new HashSet<>();
        
        String roleName = request.getRoleContext() == RoleContext.SUPER_ADMIN ? 
                AppConstants.Roles.ROLE_SUPER_ADMIN : 
                AppConstants.Roles.ROLE_ADMIN;

        // Si es un rol específico que no sea ADMIN/SUPER_ADMIN, podríamos expandir esto
        // Por ahora mantenemos la lógica de bootstrap/domain services
        
        roleRepository.findByName(roleName).ifPresent(rolesToAssign::add);
        
        if (rolesToAssign.isEmpty()) {
            log.warn("⚠️ [PROVISIONING] No se encontró el rol '{}' para asignar", roleName);
            return new HashSet<>();
        }

        return userTenantRoleService.assignRolesToUserAndTenant(user, request.getTenant().getId(), rolesToAssign);
    }
}
