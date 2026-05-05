package com.amachi.app.vitalia.medicalcore.encounter.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.encounter.dto.ConditionDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Condition;
import org.mapstruct.*;

/**
 * Enterprise Mapper para diagnósticos y condiciones clínicas (SaaS Elite Tier).
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ConditionMapper extends EntityDtoMapper<Condition, ConditionDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "encounter.id", source = "encounterId")
    @Mapping(target = "doctor.id", source = "doctorId")
//    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "icd10.id", source = "icd10Id")
    @Mapping(target = "episodeOfCare.id", source = "episodeOfCareId")
//    @Mapping(target = "isDeleted", ignore = true)
    Condition toEntity(ConditionDto dto);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + entity.getPatient().getPerson().getLastName() : null)")
    @Mapping(target = "encounterId", source = "encounter.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + entity.getDoctor().getPerson().getLastName() : null)")
//    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(target = "icd10Id", source = "icd10.id")
    @Mapping(target = "icd10Code", source = "icd10.code")
    @Mapping(target = "episodeOfCareId", source = "episodeOfCare.id")
    ConditionDto toDto(Condition entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "encounter.id", source = "encounterId")
    @Mapping(target = "doctor.id", source = "doctorId")
//    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "icd10.id", source = "icd10Id")
    @Mapping(target = "episodeOfCare.id", source = "episodeOfCareId")
//    @Mapping(target = "isDeleted", ignore = true)
    void updateEntityFromDto(ConditionDto dto, @MappingTarget Condition entity);
}
