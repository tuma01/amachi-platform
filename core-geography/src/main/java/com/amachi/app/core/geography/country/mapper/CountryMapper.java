package com.amachi.app.core.geography.country.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.country.dto.CountryDto;
import com.amachi.app.core.geography.country.entity.Country;
import org.mapstruct.Mapper;

import org.mapstruct.Builder;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface CountryMapper extends EntityDtoMapper<Country, CountryDto> {

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    Country toEntity(CountryDto dto);

    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CountryDto dto, @MappingTarget Country entity);

    @Override
    @BeanMapping(unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
    CountryDto toDto(Country entity);
}
