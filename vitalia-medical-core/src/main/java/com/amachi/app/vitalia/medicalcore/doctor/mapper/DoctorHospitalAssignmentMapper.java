package com.amachi.app.vitalia.medicalcore.doctor.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorHospitalAssignmentDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.DoctorHospitalAssignment;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface DoctorHospitalAssignmentMapper extends EntityDtoMapper<DoctorHospitalAssignment, DoctorHospitalAssignmentDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "doctor.id",   source = "doctorId")
    @Mapping(target = "hospital.id", source = "hospitalId")
    DoctorHospitalAssignment toEntity(DoctorHospitalAssignmentDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "doctorId",       source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + (entity.getDoctor().getPerson().getLastName() != null ? entity.getDoctor().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "hospitalId",     source = "hospital.id")
    @Mapping(target = "hospitalName",   source = "hospital.legalName")
    DoctorHospitalAssignmentDto toDto(DoctorHospitalAssignment entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "doctor.id",   source = "doctorId")
    @Mapping(target = "hospital.id", source = "hospitalId")
    void updateEntityFromDto(DoctorHospitalAssignmentDto dto, @MappingTarget DoctorHospitalAssignment existing);
}
