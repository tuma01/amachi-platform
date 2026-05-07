package com.amachi.app.vitalia.medicalcore.prescription.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.prescription.dto.PrescriptionDto;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface PrescriptionMapper extends EntityDtoMapper<Prescription, PrescriptionDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",    source = "patientId")
    @Mapping(target = "doctor.id",     source = "doctorId")
    @Mapping(target = "encounter.id",  source = "encounterId")
    @Mapping(target = "medication.id", source = "medicationId")
    Prescription toEntity(PrescriptionDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId",       source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "doctorId",        source = "doctor.id")
    @Mapping(target = "doctorFullName",  expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + (entity.getDoctor().getPerson().getLastName() != null ? entity.getDoctor().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "encounterId",     source = "encounter.id")
    @Mapping(target = "medicationId",    source = "medication.id")
    PrescriptionDto toDto(Prescription entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient.id",    source = "patientId")
    @Mapping(target = "doctor.id",     source = "doctorId")
    @Mapping(target = "encounter.id",  source = "encounterId")
    @Mapping(target = "medication.id", source = "medicationId")
    void updateEntityFromDto(PrescriptionDto dto, @MappingTarget Prescription existing);
}
