package com.amachi.app.core.geography.state.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.state.dto.StateDto;
import com.amachi.app.core.geography.state.entity.State;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface StateMapper extends EntityDtoMapper<State, StateDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "country.id", source = "countryId")
    State toEntity(StateDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "country.id", source = "countryId")
    void updateEntityFromDto(StateDto dto, @MappingTarget State entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "countryId", source = "country.id")
    StateDto toDto(State entity);
}

