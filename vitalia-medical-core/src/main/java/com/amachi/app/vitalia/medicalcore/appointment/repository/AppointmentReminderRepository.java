package com.amachi.app.vitalia.medicalcore.appointment.repository;

import com.amachi.app.core.common.enums.ReminderStatus;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.appointment.entity.AppointmentReminder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentReminderRepository extends TenantCommonRepository<AppointmentReminder, Long> {

    List<AppointmentReminder> findByAppointmentIdAndTenantId(Long appointmentId, Long tenantId);

    List<AppointmentReminder> findByAppointmentIdAndStatusAndTenantId(Long appointmentId, ReminderStatus status, Long tenantId);
}
