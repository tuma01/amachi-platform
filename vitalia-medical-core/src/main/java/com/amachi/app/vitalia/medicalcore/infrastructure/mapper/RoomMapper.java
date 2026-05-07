package com.amachi.app.vitalia.medicalcore.infrastructure.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.RoomDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface RoomMapper extends EntityDtoMapper<Room, RoomDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "beds", ignore = true)
    Room toEntity(RoomDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "unitId",   source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "bedCount", expression = "java(entity.getBeds() != null ? entity.getBeds().size() : 0)")
    RoomDto toDto(Room entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "beds", ignore = true)
    void updateEntityFromDto(RoomDto dto, @MappingTarget Room entity);
}
