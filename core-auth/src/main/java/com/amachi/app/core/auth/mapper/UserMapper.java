package com.amachi.app.core.auth.mapper;

import com.amachi.app.core.auth.dto.UserDto;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true), uses = {
        UserAccountMapper.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper extends EntityDtoMapper<User, UserDto> {

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    User toEntity(UserDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "password", ignore = true) // Security Safeguard
    void updateEntityFromDto(UserDto dto, @MappingTarget User entity);


    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "personId", expression = "java(entity.getPerson() != null ? entity.getPerson().getId() : null)")
    UserDto toDto(User entity);
}
