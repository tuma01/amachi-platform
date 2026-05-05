package com.amachi.app.core.management.provisioning.dto;

import com.amachi.app.core.auth.entity.UserAccount;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.auth.entity.User;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * Resultado del ciclo completo de provisioning.
 * Contiene las entidades creadas/resueltas para uso posterior si se necesitan.
 */
@Value
@Builder
public class ProvisioningResult {

    Person person;
    User user;
    UserAccount userAccount;
    Set<UserTenantRole> roles;
}
