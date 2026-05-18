package com.amachi.app.vitalia.medicalcore.hospitalization.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.hospitalization.dto.HospitalizationDto;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface HospitalizationMapper extends EntityDtoMapper<Hospitalization, HospitalizationDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient",   ignore = true)
    @Mapping(target = "doctor",    ignore = true)
    @Mapping(target = "nurse",     ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "unit",      ignore = true)
    @Mapping(target = "room",      ignore = true)
    @Mapping(target = "bed",       ignore = true)
    @Mapping(target = "insurance", ignore = true)
    Hospitalization toEntity(HospitalizationDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",       source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + entity.getPatient().getPerson().getLastName() : null)")
    @Mapping(target = "doctorId",        source = "doctor.id")
    @Mapping(target = "doctorFullName",  expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + entity.getDoctor().getPerson().getLastName() : null)")
    @Mapping(target = "nurseId",         source = "nurse.id")
    @Mapping(target = "nurseFullName",   expression = "java(entity.getNurse() != null && entity.getNurse().getPerson() != null ? entity.getNurse().getPerson().getFirstName() + \" \" + entity.getNurse().getPerson().getLastName() : null)")
    @Mapping(target = "encounterId",     source = "encounter.id")
    @Mapping(target = "unitId",          source = "unit.id")
    @Mapping(target = "unitName",        source = "unit.name")
    @Mapping(target = "roomId",          source = "room.id")
    @Mapping(target = "roomNumber",      source = "room.roomNumber")
    @Mapping(target = "bedId",           source = "bed.id")
    @Mapping(target = "bedCode",         source = "bed.bedCode")
    @Mapping(target = "insuranceId",     source = "insurance.id")
    @Mapping(target = "providerName",    source = "insurance.provider.name")
    @Mapping(target = "policyNumber",    source = "insurance.policyNumber")
    @Mapping(target = "lengthOfStayInDays", expression = "java(entity.getLengthOfStayInDays())")
    HospitalizationDto toDto(Hospitalization entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient",   ignore = true)
    @Mapping(target = "doctor",    ignore = true)
    @Mapping(target = "nurse",     ignore = true)
    @Mapping(target = "encounter", ignore = true)
    @Mapping(target = "unit",      ignore = true)
    @Mapping(target = "room",      ignore = true)
    @Mapping(target = "bed",       ignore = true)
    @Mapping(target = "insurance", ignore = true)
    void updateEntityFromDto(HospitalizationDto dto, @MappingTarget Hospitalization entity);
}
