package com.amachi.app.vitalia.medicalcore.patient.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.patient.dto.PatientDto;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.entity.PatientDetails;
import org.mapstruct.*;

/**
 * Enterprise Mapper para la gestión de pacientes (SaaS Elite Tier).
 * Simplificado para sincronizarse con el modelo MVP estable.
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface PatientMapper extends EntityDtoMapper<Patient, PatientDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
//    @AuditableIgnoreConfig.IgnoreSoftDelete
    @Mapping(target = "details.weight", source = "weight")
    @Mapping(target = "details.height", source = "height")
    @Mapping(target = "details.bloodType.id", source = "bloodTypeId")
    @Mapping(target = "details.hasDisability", source = "hasDisability")
    @Mapping(target = "details.disabilityDetails", source = "disabilityDetails")
    @Mapping(target = "details.isPregnant", source = "isPregnant")
    @Mapping(target = "details.gestationalWeeks", source = "gestationalWeeks")
    @Mapping(target = "details.childrenCount", source = "childrenCount")
    @Mapping(target = "details.ethnicGroup", source = "ethnicGroup")
    @Mapping(target = "active", source = "active") // ✅ FIX
    Patient toEntity(PatientDto dto);

    @Override
    @Mapping(target = "patientFullName", expression = "java(entity.getPerson() != null ? entity.getPerson().getFirstName() + \" \" + (entity.getPerson().getLastName() != null ? entity.getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "weight", source = "details.weight")
    @Mapping(target = "height", source = "details.height")
    @Mapping(target = "bloodTypeId", source = "details.bloodType.id")
    @Mapping(target = "bloodTypeName", source = "details.bloodType.name")
    @Mapping(target = "hasDisability", source = "details.hasDisability")
    @Mapping(target = "disabilityDetails", source = "details.disabilityDetails")
    @Mapping(target = "isPregnant", source = "details.isPregnant")
    @Mapping(target = "gestationalWeeks", source = "details.gestationalWeeks")
    @Mapping(target = "childrenCount", source = "details.childrenCount")
    @Mapping(target = "ethnicGroup", source = "details.ethnicGroup")
    @Mapping(target = "active", source = "active") // ✅ FIX
    PatientDto toDto(Patient entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "details.weight", source = "weight")
    @Mapping(target = "details.height", source = "height")
    @Mapping(target = "details.bloodType.id", source = "bloodTypeId")
    @Mapping(target = "details.hasDisability", source = "hasDisability")
    @Mapping(target = "details.disabilityDetails", source = "disabilityDetails")
    @Mapping(target = "details.isPregnant", source = "isPregnant")
    @Mapping(target = "details.gestationalWeeks", source = "gestationalWeeks")
    @Mapping(target = "details.childrenCount", source = "childrenCount")
    @Mapping(target = "details.ethnicGroup", source = "ethnicGroup")
    @Mapping(target = "active", source = "active") // ✅ FIX
    void updateEntityFromDto(PatientDto dto, @MappingTarget Patient entity);

    @AfterMapping
    default void ensureDetails(@MappingTarget Patient entity) {
        if (entity.getDetails() == null) {
            entity.setDetails(new PatientDetails());
        }
    }
}
