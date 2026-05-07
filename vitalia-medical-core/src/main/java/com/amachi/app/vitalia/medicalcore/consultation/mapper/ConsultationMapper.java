package com.amachi.app.vitalia.medicalcore.consultation.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.consultation.dto.ConsultationDto;
import com.amachi.app.vitalia.medicalcore.consultation.entity.Consultation;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ConsultationMapper extends EntityDtoMapper<Consultation, ConsultationDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient",        ignore = true)
    @Mapping(target = "doctor",         ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "type",           ignore = true)
    Consultation toEntity(ConsultationDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",       source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + entity.getPatient().getPerson().getLastName() : null)")
    @Mapping(target = "doctorId",        source = "doctor.id")
    @Mapping(target = "doctorFullName",  expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + entity.getDoctor().getPerson().getLastName() : null)")
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(target = "typeId",          source = "type.id")
    @Mapping(target = "typeName",        source = "type.name")
    ConsultationDto toDto(Consultation entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient",        ignore = true)
    @Mapping(target = "doctor",         ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "type",           ignore = true)
    void updateEntityFromDto(ConsultationDto dto, @MappingTarget Consultation entity);
}
