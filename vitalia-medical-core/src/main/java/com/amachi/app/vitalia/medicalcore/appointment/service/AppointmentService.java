package com.amachi.app.vitalia.medicalcore.appointment.service;

import com.amachi.app.core.common.enums.AppointmentStatus;
import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.appointment.dto.AppointmentDto;
import com.amachi.app.vitalia.medicalcore.appointment.dto.search.AppointmentSearchDto;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;

/**
 * Interfaz de servicio avanzada (SaaS Global Tier).
 * Maneja tiempos con OffsetDateTime para soporte multi-región.
 */
public interface AppointmentService extends GenericService<Appointment, AppointmentDto, AppointmentSearchDto> {

//    Appointment updateStatus(Long id, AppointmentStatus status);
//
//    Appointment reschedule(Long id, OffsetDateTime newStartTime, OffsetDateTime newEndTime);
//
//    Appointment cancel(Long id, String reason);
//
//    Appointment registerNoShow(Long id);

    Appointment checkIn(Long id);

    Appointment updateStatus(Long id, AppointmentStatus status);

//    void validateCollisions(Appointment appointment);
//
//    /**
//     * Adquirir un bloqueo suave para edición segura (Optimistic Multi-Operator).
//     */
//    void lockAppointment(Long id, String operatorName, int minutes);
}
