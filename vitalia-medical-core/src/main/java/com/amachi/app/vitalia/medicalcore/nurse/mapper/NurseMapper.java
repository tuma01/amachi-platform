package com.amachi.app.vitalia.medicalcore.nurse.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.nurse.dto.NurseDto;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface NurseMapper extends EntityDtoMapper<Nurse, NurseDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person",         ignore = true)
    @Mapping(target = "departmentUnit", ignore = true)
    @Mapping(target = "userProfile",    ignore = true)
    Nurse toEntity(NurseDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "personId",           source = "person.id")
    @Mapping(target = "personFullName",     expression = "java(entity.getPerson() != null ? entity.getPerson().getFirstName() + \" \" + (entity.getPerson().getLastName() != null ? entity.getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "departmentUnitId",   source = "departmentUnit.id")
    @Mapping(target = "departmentUnitName", source = "departmentUnit.name")
    @Mapping(target = "userProfileId",      source = "userProfile.id")
    NurseDto toDto(Nurse entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person",         ignore = true)
    @Mapping(target = "departmentUnit", ignore = true)
    @Mapping(target = "userProfile",    ignore = true)
    void updateEntityFromDto(NurseDto dto, @MappingTarget Nurse existing);
}
