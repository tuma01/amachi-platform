package com.amachi.app.vitalia.medicalcore.profile.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.profile.dto.UserProfileDto;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface UserProfileMapper extends EntityDtoMapper<UserProfile, UserProfileDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "photo", ignore = true)
    UserProfile toEntity(UserProfileDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    UserProfileDto toDto(UserProfile entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "photo", ignore = true)
    void updateEntityFromDto(UserProfileDto dto, @MappingTarget UserProfile existing);
}
