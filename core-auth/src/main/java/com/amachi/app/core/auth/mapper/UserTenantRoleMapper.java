package com.amachi.app.core.auth.mapper;

import com.amachi.app.core.auth.dto.UserTenantRoleDto;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true), nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserTenantRoleMapper extends EntityDtoMapper<UserTenantRole, UserTenantRoleDto> {

        @Override
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        UserTenantRole toEntity(UserTenantRoleDto dto);

        @Override
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        UserTenantRoleDto toDto(UserTenantRole entity);

        @Override
        @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
        @Mapping(target = "id", ignore = true)
        void updateEntityFromDto(UserTenantRoleDto dto, @MappingTarget UserTenantRole entity);
}
