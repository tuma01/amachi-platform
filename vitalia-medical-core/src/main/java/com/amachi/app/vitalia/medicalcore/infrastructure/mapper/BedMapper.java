package com.amachi.app.vitalia.medicalcore.infrastructure.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.BedDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface BedMapper extends EntityDtoMapper<Bed, BedDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "room", ignore = true)
    Bed toEntity(BedDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "roomId",     source = "room.id")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    BedDto toDto(Bed entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "room", ignore = true)
    void updateEntityFromDto(BedDto dto, @MappingTarget Bed entity);
}
