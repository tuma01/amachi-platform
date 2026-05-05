package com.amachi.app.vitalia.medicalcore.encounter.mapper;

import com.amachi.app.core.common.enums.VisitTypeEnum;
import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterType;
import com.amachi.app.vitalia.medicalcore.encounter.dto.EncounterDto;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import org.mapstruct.*;

/**
 * Enterprise Mapper for Medical Encounters (SaaS Elite Tier).
 * Resolves all unmapped clinical properties and maintains FHIR compliance.
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface EncounterMapper extends EntityDtoMapper<Encounter, EncounterDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @Mapping(target = "patient.id",        source = "patientId")
    @Mapping(target = "doctor.id",         source = "doctorId")
//    @Mapping(target = "medicalHistory.id",  source = "medicalHistoryId")
    @Mapping(target = "appointment.id",     source = "appointmentId")
    @Mapping(target = "episodeOfCare.id",   source = "episodeOfCareId")
//    @Mapping(target = "consultationType.id", source = "consultationTypeId")
//    @Mapping(target = "isDeleted",          ignore = true)
    Encounter toEntity(EncounterDto dto);

    @Override
    @Mapping(target = "patientId",         source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + entity.getPatient().getPerson().getLastName() : null)")
    @Mapping(target = "doctorId",          source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + entity.getDoctor().getPerson().getLastName() : null)")
    @Mapping(target = "appointmentId",     source = "appointment.id")
//    @Mapping(target = "medicalHistoryId",  source = "medicalHistory.id")
    @Mapping(target = "episodeOfCareId",   source = "episodeOfCare.id")
//    @Mapping(target = "consultationTypeId", source = "consultationType.id")
    EncounterDto toDto(Encounter entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "patient.id",        source = "patientId")
    @Mapping(target = "doctor.id",         source = "doctorId")
//    @Mapping(target = "medicalHistory.id",  source = "medicalHistoryId")
    @Mapping(target = "appointment.id",     source = "appointmentId")
    @Mapping(target = "episodeOfCare.id",   source = "episodeOfCareId")
//    @Mapping(target = "consultationType.id", source = "consultationTypeId")
//    @Mapping(target = "isDeleted",          ignore = true)
    void updateEntityFromDto(EncounterDto dto, @MappingTarget Encounter entity);

    @ValueMappings({
            @ValueMapping(source = "CONSULTA_EXTERNA",      target = "OUTPATIENT"),
            @ValueMapping(source = "VISITA_EMERGENCIA",     target = "EMERGENCY"),
            @ValueMapping(source = "INGRESO_HOSPITALARIO",   target = "INPATIENT"),
            @ValueMapping(source = "TELECONSULTA",          target = "TELEHEALTH"),
            @ValueMapping(source = "VISITA_ADMINISTRATIVA", target = "ADMINISTRATIVE"),
            @ValueMapping(source = "OTRO",                 target = "OUTPATIENT"),
            @ValueMapping(source = MappingConstants.ANY_UNMAPPED, target = "OUTPATIENT")
    })
    EncounterType toEncounterType(VisitTypeEnum encounterType);

    @ValueMappings({
            @ValueMapping(source = "OUTPATIENT",      target = "CONSULTA_EXTERNA"),
            @ValueMapping(source = "EMERGENCY",       target = "VISITA_EMERGENCIA"),
            @ValueMapping(source = "INPATIENT",       target = "INGRESO_HOSPITALARIO"),
            @ValueMapping(source = "TELEHEALTH",      target = "TELECONSULTA"),
            @ValueMapping(source = "ADMINISTRATIVE",  target = "VISITA_ADMINISTRATIVA"),
            @ValueMapping(source = MappingConstants.ANY_REMAINING, target = "OTRO")
    })
    VisitTypeEnum toVisitType(EncounterType encounterType);
}
