package com.amachi.app.vitalia.medicalcore.medicalhistory.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.medicalhistory.dto.MedicalHistoryDto;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface MedicalHistoryMapper extends EntityDtoMapper<MedicalHistory, MedicalHistoryDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",          source = "patientId")
    @Mapping(target = "responsibleDoctor.id", source = "responsibleDoctorId")
    @Mapping(target = "person",              ignore = true)
    MedicalHistory toEntity(MedicalHistoryDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",                   source = "patient.id")
    @Mapping(target = "patientFullName",              expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "responsibleDoctorId",          source = "responsibleDoctor.id")
    @Mapping(target = "responsibleDoctorFullName",    expression = "java(entity.getResponsibleDoctor() != null && entity.getResponsibleDoctor().getPerson() != null ? entity.getResponsibleDoctor().getPerson().getFirstName() + \" \" + (entity.getResponsibleDoctor().getPerson().getLastName() != null ? entity.getResponsibleDoctor().getPerson().getLastName() : \"\") : null)")
    MedicalHistoryDto toDto(MedicalHistory entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",          source = "patientId")
    @Mapping(target = "responsibleDoctor.id", source = "responsibleDoctorId")
    @Mapping(target = "person",              ignore = true)
    void updateEntityFromDto(MedicalHistoryDto dto, @MappingTarget MedicalHistory existing);
}
