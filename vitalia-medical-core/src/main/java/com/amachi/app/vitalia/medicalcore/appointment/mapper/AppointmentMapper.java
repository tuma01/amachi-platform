package com.amachi.app.vitalia.medicalcore.appointment.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import org.mapstruct.*;

/**
 * Enterprise Mapper para la gestión de agendas médicas (SaaS Elite Tier).
 * Simplificado para sincronizarse con el modelo MVP estable.
 */
@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface AppointmentMapper extends EntityDtoMapper<Appointment, AppointmentDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
    Appointment toEntity(AppointmentDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(entity.getPatient() != null && entity.getPatient().getPerson() != null ? entity.getPatient().getPerson().getFirstName() + \" \" + (entity.getPatient().getPerson().getLastName() != null ? entity.getPatient().getPerson().getLastName() : \"\") : null)")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(entity.getDoctor() != null && entity.getDoctor().getPerson() != null ? entity.getDoctor().getPerson().getFirstName() + \" \" + (entity.getDoctor().getPerson().getLastName() != null ? entity.getDoctor().getPerson().getLastName() : \"\") : null)")
    AppointmentDto toDto(Appointment entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
    void updateEntityFromDto(AppointmentDto dto, @MappingTarget Appointment entity);
}
