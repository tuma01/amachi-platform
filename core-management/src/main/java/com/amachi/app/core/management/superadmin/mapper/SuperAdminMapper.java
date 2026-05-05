package com.amachi.app.core.management.superadmin.mapper;

import com.amachi.app.core.auth.mapper.UserMapper;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.domain.mapper.PersonTenantMapper;
import com.amachi.app.core.management.superadmin.dto.SuperAdminDto;
import com.amachi.app.core.management.superadmin.entity.SuperAdmin;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true), uses = {
        AddressMapper.class,
        PersonTenantMapper.class,
        UserMapper.class
})
public interface SuperAdminMapper extends EntityDtoMapper<SuperAdmin, SuperAdminDto> {

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    SuperAdmin toEntity(SuperAdminDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    void updateEntityFromDto(SuperAdminDto dto, @MappingTarget SuperAdmin entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    SuperAdminDto toDto(SuperAdmin entity);
}
