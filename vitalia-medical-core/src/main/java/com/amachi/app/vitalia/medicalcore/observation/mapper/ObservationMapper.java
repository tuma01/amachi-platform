package com.amachi.app.vitalia.medicalcore.observation.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.observation.dto.ObservationDto;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import org.mapstruct.*;

/**
 * Professional Mapper for clinical observations and vital signs (SaaS Elite Tier).
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface ObservationMapper extends EntityDtoMapper<Observation, ObservationDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "encounter.id", source = "encounterId")
    @Mapping(target = "doctor.id", source = "doctorId")
    // ✔ explicit clinical mappings
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "interpretation", source = "interpretation")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "effectiveDateTime", source = "effectiveDateTime")
    Observation toEntity(ObservationDto dto);

    @Override
    @BeanMapping(unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + entity.getPatient().getPerson().getLastName() : null)")
    @Mapping(target = "encounterId", source = "encounter.id")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + entity.getDoctor().getPerson().getLastName() : null)")
    // ✔ explicit clinical mappings
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "interpretation", source = "interpretation")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "effectiveDateTime", source = "effectiveDateTime")
    ObservationDto toDto(Observation entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "encounter.id", source = "encounterId")
    @Mapping(target = "doctor.id", source = "doctorId")
    // ✔ also here
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "value", source = "value")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "interpretation", source = "interpretation")
    @Mapping(target = "notes", source = "notes")
    void updateEntityFromDto(ObservationDto dto, @MappingTarget Observation entity);
}
