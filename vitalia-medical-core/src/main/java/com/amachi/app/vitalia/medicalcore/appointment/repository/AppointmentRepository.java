package com.amachi.app.vitalia.medicalcore.appointment.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.appointment.entity.Appointment;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

/**
 * Persistencia simplificada para validación de flujo funcional (Fase 1).
 */
@Repository
public interface AppointmentRepository extends TenantCommonRepository<Appointment, Long> {

    boolean existsByDoctorIdAndTenantIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long doctorId,
            Long tenantId,
            OffsetDateTime end,
            OffsetDateTime start
    );
}
