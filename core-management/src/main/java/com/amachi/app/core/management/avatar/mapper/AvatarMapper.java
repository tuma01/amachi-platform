package com.amachi.app.core.management.avatar.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.management.avatar.dto.AvatarDto;
import com.amachi.app.core.management.avatar.entity.Avatar;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true),
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AvatarMapper extends EntityDtoMapper<Avatar, AvatarDto> {

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "content", ignore = true)
    Avatar toEntity(AvatarDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "userId", source = "user.id")
    AvatarDto toDto(Avatar entity);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE,
                 nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "content", ignore = true)
    void updateEntityFromDto(AvatarDto dto, @MappingTarget Avatar entity);
}
