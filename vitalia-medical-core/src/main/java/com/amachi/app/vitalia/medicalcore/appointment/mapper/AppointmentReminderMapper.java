package com.amachi.app.vitalia.medicalcore.appointment.mapper;

import com.amachi.app.core.common.mapper.AuditableIgnoreConfig;
import com.amachi.app.core.common.mapper.BaseMapperConfig;
import com.amachi.app.core.common.mapper.EntityDtoMapper;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentReminderDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;
import org.mapstruct.*;

@Mapper(config = BaseMapperConfig.class, builder = @Builder(disableBuilder = true))
public interface AppointmentReminderMapper extends EntityDtoMapper<AppointmentReminder, AppointmentReminderDto> {

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "appointment.id", source = "appointmentId")
    AppointmentReminder toEntity(AppointmentReminderDto dto);

    @Override
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "appointmentId", source = "appointment.id")
    AppointmentReminderDto toDto(AppointmentReminder entity);

    @Override
    @AuditableIgnoreConfig.IgnoreTenantAuditableFields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
                 unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "appointment.id", source = "appointmentId")
    void updateEntityFromDto(AppointmentReminderDto dto, @MappingTarget AppointmentReminder existing);
}
