package com.amachi.app.core.domain.mapper;

import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.domain.dto.PersonTenantDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.enums.RelationStatus;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.domain.entity.PersonTenant;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface PersonTenantMapper  extends EntityDtoMapper<PersonTenant, PersonTenantDto> {

    @Named("personTenantSetFromIdsForSuperAdmin")
    default Set<PersonTenant> personTenantSetFromIdsForSuperAdmin(Set<Long> tenantIds) {
        return createPersonTenants(tenantIds, RoleContext.SUPER_ADMIN);
    }

    @Named("personTenantSetFromIdsForTenantAdmin")
    default Set<PersonTenant> personTenantSetFromIdsForTenantAdmin(Set<Long> tenantIds) {
        return createPersonTenants(tenantIds, RoleContext.ADMIN);
    }

    @Named("personTenantSetToIds")
    default Set<Long> personTenantSetToIds(Set<PersonTenant> personTenants) {
        if (personTenants == null) {
            return new HashSet<>();
        }
        return personTenants.stream()
                .filter(pt -> pt.getTenant() != null)
                .map(pt -> pt.getTenant().getId())
                .collect(Collectors.toSet());
    }

    default Set<PersonTenant> createPersonTenants(Set<Long> tenantIds, RoleContext context) {
        if (tenantIds == null) {
            return new HashSet<>();
        }
        return tenantIds.stream().map(id -> {
            Tenant tenant = new Tenant();
            tenant.setId(id);
            PersonTenant pt = new PersonTenant();
            pt.setTenant(tenant);
            pt.setRelationStatus(RelationStatus.ACTIVE);
            pt.setRoleContext(context);
            pt.setDateRegistered(LocalDateTime.now());
            return pt;
        }).collect(Collectors.toSet());
    }
}
