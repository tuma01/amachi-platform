package com.amachi.app.vitalia.medicalcore.medicationdispense.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.medicationdispense.dto.MedicationDispenseDto;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface MedicationDispenseMapper extends EntityDtoMapper<MedicationDispense, MedicationDispenseDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",      source = "patientId")
    @Mapping(target = "prescription.id", source = "prescriptionId")
    @Mapping(target = "encounter.id",    source = "encounterId")
    @Mapping(target = "dispenser.id",    source = "dispenserId")
    @Mapping(target = "medication.id",   source = "medicationId")
    MedicationDispense toEntity(MedicationDispenseDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",        source = "patient.id")
    @Mapping(target = "patientFullName",  expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "prescriptionId",   source = "prescription.id")
    @Mapping(target = "encounterId",      source = "encounter.id")
    @Mapping(target = "dispenserId",      source = "dispenser.id")
    @Mapping(target = "dispenserFullName",expression = "java(entity.getDispenser() != null && entity.getDispenser().getPerson() != null ? entity.getDispenser().getPerson().getFirstName() + \" \" + (entity.getDispenser().getPerson().getLastName() != null ? entity.getDispenser().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "medicationId",     source = "medication.id")
    @Mapping(target = "medicationName",   source = "medication.genericName")
    MedicationDispenseDto toDto(MedicationDispense entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",      source = "patientId")
    @Mapping(target = "prescription.id", source = "prescriptionId")
    @Mapping(target = "encounter.id",    source = "encounterId")
    @Mapping(target = "dispenser.id",    source = "dispenserId")
    @Mapping(target = "medication.id",   source = "medicationId")
    void updateEntityFromDto(MedicationDispenseDto dto, @MappingTarget MedicationDispense existing);
}
