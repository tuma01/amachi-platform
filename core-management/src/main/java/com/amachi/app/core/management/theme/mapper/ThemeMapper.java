package com.amachi.app.core.management.theme.mapper;

import com.amachi.app.core.domain.theme.entity.Theme;
import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.domain.theme.dto.ThemeDto;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ThemeMapper extends EntityDtoMapper<Theme, ThemeDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    Theme toEntity(ThemeDto dto);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    ThemeDto toDto(Theme entity);

    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "allowCustomCss", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "faviconUrl", ignore = true)
    @Mapping(target = "customCss", ignore = true)
    void updateEntityFromDto(ThemeDto dto, @MappingTarget Theme theme);
}
