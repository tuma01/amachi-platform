package com.amachi.app.vitalia.medicalcore.doctor.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import org.mapstruct.*;

/**
 * Enterprise Mapper para la gestión de facultativos (SaaS Elite Tier).
 * Simplificado para sincronizarse con el modelo MVP estable.
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface DoctorMapper extends EntityDtoMapper<Doctor, DoctorDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
//    @Mapping(target = "departmentUnit.id", source = "departmentUnitId")
//    @Mapping(target = "employee.id",       source = "employeeId")
//    @Mapping(target = "isDeleted",          ignore = true)
    @Mapping(target = "person", ignore = true)
    Doctor toEntity(DoctorDto dto);

    @Override
    @Mapping(target = "doctorFullName", expression = "java(entity.getPerson() != null ? entity.getPerson().getFirstName() + \" \" + (entity.getPerson().getLastName() != null ? entity.getPerson().getLastName() : \"\") : null)")
//    @Mapping(target = "departmentUnitId", source = "departmentUnit.id")
//    @Mapping(target = "departmentUnitName", source = "departmentUnit.name")
//    @Mapping(target = "employeeId",       source = "employee.id")
    DoctorDto toDto(Doctor entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(target = "departmentUnit.id", source = "departmentUnitId")
//    @Mapping(target = "employee.id",       source = "employeeId")
//    @Mapping(target = "isDeleted",          ignore = true)
    @Mapping(target = "person", ignore = true)
    void updateEntityFromDto(DoctorDto dto, @MappingTarget Doctor existing);
}
