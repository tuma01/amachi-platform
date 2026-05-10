package com.amachi.app.vitalia.medicalcore.medicationadministration.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.medicationadministration.dto.MedicationAdministrationDto;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface MedicationAdministrationMapper extends EntityDtoMapper<MedicationAdministration, MedicationAdministrationDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",      source = "patientId")
    @Mapping(target = "prescription.id", source = "prescriptionId")
    @Mapping(target = "dispense.id",     source = "dispenseId")
    @Mapping(target = "encounter.id",    source = "encounterId")
    @Mapping(target = "nurse.id",        source = "nurseId")
    MedicationAdministration toEntity(MedicationAdministrationDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",       source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "prescriptionId",  source = "prescription.id")
    @Mapping(target = "dispenseId",      source = "dispense.id")
    @Mapping(target = "encounterId",     source = "encounter.id")
    @Mapping(target = "nurseId",         source = "nurse.id")
    @Mapping(target = "nurseFullName",   expression = "java(entity.getNurse() != null && entity.getNurse().getPerson() != null ? entity.getNurse().getPerson().getFirstName() + \" \" + (entity.getNurse().getPerson().getLastName() != null ? entity.getNurse().getPerson().getLastName() : \"\") : null)")
    MedicationAdministrationDto toDto(MedicationAdministration entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",      source = "patientId")
    @Mapping(target = "prescription.id", source = "prescriptionId")
    @Mapping(target = "dispense.id",     source = "dispenseId")
    @Mapping(target = "encounter.id",    source = "encounterId")
    @Mapping(target = "nurse.id",        source = "nurseId")
    void updateEntityFromDto(MedicationAdministrationDto dto, @MappingTarget MedicationAdministration existing);
}
