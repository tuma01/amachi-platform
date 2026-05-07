package com.amachi.app.vitalia.medicalcore.employee.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.employee.dto.EmployeeDto;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface EmployeeMapper extends EntityDtoMapper<Employee, EmployeeDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person",         ignore = true)
    @Mapping(target = "departmentUnit", ignore = true)
    Employee toEntity(EmployeeDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "personId",           source = "person.id")
    @Mapping(target = "personFullName",     expression = "java(entity.getPerson() != null ? entity.getPerson().getFirstName() + \" \" + (entity.getPerson().getLastName() != null ? entity.getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "departmentUnitId",   source = "departmentUnit.id")
    @Mapping(target = "departmentUnitName", source = "departmentUnit.name")
    EmployeeDto toDto(Employee entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "person",         ignore = true)
    @Mapping(target = "departmentUnit", ignore = true)
    void updateEntityFromDto(EmployeeDto dto, @MappingTarget Employee existing);
}
