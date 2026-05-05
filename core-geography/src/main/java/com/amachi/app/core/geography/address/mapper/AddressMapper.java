package com.amachi.app.core.geography.address.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface AddressMapper extends EntityDtoMapper<Address, AddressDto> {

    // Geographic FK references (country, state, province, municipality) are ignored in write
    // mappings. Hibernate 6.x rejects shell objects (id set, version=null) as detached entities.
    // The service resolves these via em.getReference() to get valid managed proxies.
    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @Mapping(target = "country",      ignore = true)
    @Mapping(target = "state",        ignore = true)
    @Mapping(target = "province",     ignore = true)
    @Mapping(target = "municipality", ignore = true)
    Address toEntity(AddressDto dto);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "countryId",      source = "country.id")
    @Mapping(target = "stateId",        source = "state.id")
    @Mapping(target = "provinceId",     source = "province.id")
    @Mapping(target = "municipalityId", source = "municipality.id")
    AddressDto toDto(Address entity);

    @Override
    @AuditableIgnoreConfig.IgnorePureAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id",           ignore = true)
    @Mapping(target = "country",      ignore = true)
    @Mapping(target = "state",        ignore = true)
    @Mapping(target = "province",     ignore = true)
    @Mapping(target = "municipality", ignore = true)
    void updateEntityFromDto(AddressDto dto, @MappingTarget Address entity);
}
