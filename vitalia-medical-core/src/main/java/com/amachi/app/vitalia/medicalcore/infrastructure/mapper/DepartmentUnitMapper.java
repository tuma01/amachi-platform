package com.amachi.app.vitalia.medicalcore.infrastructure.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.DepartmentUnitDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface DepartmentUnitMapper extends EntityDtoMapper<DepartmentUnit, DepartmentUnitDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "parentUnit", ignore = true)
    @Mapping(target = "subUnits",   ignore = true)
    @Mapping(target = "rooms",      ignore = true)
    @Mapping(target = "unitHead",   ignore = true)
    DepartmentUnit toEntity(DepartmentUnitDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "parentUnitId",   source = "parentUnit.id")
    @Mapping(target = "parentUnitName", source = "parentUnit.name")
    @Mapping(target = "unitHeadId",     source = "unitHead.id")
    @Mapping(target = "unitHeadName",   expression = "java(entity.getUnitHead() != null && entity.getUnitHead().getPerson() != null ? entity.getUnitHead().getPerson().getFirstName() + \" \" + entity.getUnitHead().getPerson().getLastName() : null)")
    DepartmentUnitDto toDto(DepartmentUnit entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "parentUnit", ignore = true)
    @Mapping(target = "subUnits",   ignore = true)
    @Mapping(target = "rooms",      ignore = true)
    @Mapping(target = "unitHead",   ignore = true)
    void updateEntityFromDto(DepartmentUnitDto dto, @MappingTarget DepartmentUnit entity);
}
