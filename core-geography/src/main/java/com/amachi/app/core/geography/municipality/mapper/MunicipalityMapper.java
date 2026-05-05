package com.amachi.app.core.geography.municipality.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.municipality.dto.MunicipalityDto;
import com.amachi.app.core.geography.municipality.entity.Municipality;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface MunicipalityMapper extends EntityDtoMapper<Municipality, MunicipalityDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "province.id", source = "provinceId")
    Municipality toEntity(MunicipalityDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "province.id", source = "provinceId")
    void updateEntityFromDto(MunicipalityDto dto, @MappingTarget Municipality entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "provinceId", source = "province.id")
    MunicipalityDto toDto(Municipality entity);
}
