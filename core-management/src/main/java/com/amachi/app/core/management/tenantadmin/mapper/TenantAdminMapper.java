package com.amachi.app.core.management.tenantadmin.mapper;

import com.amachi.app.core.auth.mapper.UserMapper;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.domain.mapper.PersonTenantMapper;

import com.amachi.app.core.domain.tenant.mapper.TenantMapper;
import com.amachi.app.core.management.tenantadmin.dto.TenantAdminDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.amachi.app.core.geography.address.mapper.AddressMapper;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true), nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
                AddressMapper.class,
                TenantMapper.class,
                UserMapper.class,
                PersonTenantMapper.class
})
public interface TenantAdminMapper extends EntityDtoMapper<TenantAdmin, TenantAdminDto> {

        @Override
        @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
        @Mapping(target = "createdBy", ignore = true)
        @Mapping(target = "createdDate", ignore = true)
        @Mapping(target = "lastModifiedBy", ignore = true)
        @Mapping(target = "lastModifiedDate", ignore = true)
        TenantAdmin toEntity(TenantAdminDto dto);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
        @Mapping(target = "id", ignore = true)
        @Mapping(target = "createdBy", ignore = true)
        @Mapping(target = "createdDate", ignore = true)
        @Mapping(target = "lastModifiedBy", ignore = true)
        @Mapping(target = "lastModifiedDate", ignore = true)
        void updateEntityFromDto(TenantAdminDto dto, @MappingTarget TenantAdmin entity);

        @Override
        @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
        TenantAdminDto toDto(TenantAdmin entity);
}
