package com.amachi.app.core.auth.mapper;

import com.amachi.app.core.auth.dto.RoleDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface RoleMapper extends EntityDtoMapper<Role, RoleDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    Role toEntity(RoleDto dto);

    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoleDto dto, @MappingTarget Role entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    RoleDto toDto(Role entity);
}
