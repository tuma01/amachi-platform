package com.amachi.app.core.geography.province.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.province.dto.ProvinceDto;
import com.amachi.app.core.geography.province.entity.Province;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ProvinceMapper extends EntityDtoMapper<Province, ProvinceDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "state.id", source = "stateId")
    Province toEntity(ProvinceDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "state.id", source = "stateId")
    void updateEntityFromDto(ProvinceDto dto, @MappingTarget Province entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "stateId", source = "state.id")
    ProvinceDto toDto(Province entity);
}
