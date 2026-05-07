package com.amachi.app.vitalia.medicalcore.appointment.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentReminderDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentReminderSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;

public interface AppointmentReminderService extends GenericService<AppointmentReminder, AppointmentReminderDto, AppointmentReminderSearchDto> {
}
