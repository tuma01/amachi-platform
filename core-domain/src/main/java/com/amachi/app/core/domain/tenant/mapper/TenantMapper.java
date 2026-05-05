package com.amachi.app.core.domain.tenant.mapper;

import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.theme.entity.Theme;
import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.geography.address.dto.AddressDto;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface TenantMapper extends EntityDtoMapper<Tenant, TenantDto> {

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    @Mapping(target = "theme", ignore = true)
    @Mapping(target = "addressId", source = "address.id")
    Tenant toEntity(TenantDto dto);

    @AuditableIgnoreConfig.IgnoreHybridAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "theme", ignore = true)
    @Mapping(target = "addressId", source = "address.id")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TenantDto dto, @MappingTarget Tenant entity);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "themeId", source = "theme.id", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "themeName", source = "theme.name")
    TenantDto toDto(Tenant entity);
}
