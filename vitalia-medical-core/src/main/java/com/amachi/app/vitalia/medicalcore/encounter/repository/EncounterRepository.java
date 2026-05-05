package com.amachi.app.vitalia.medicalcore.encounter.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.common.enums.EncounterStatus;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Encounter;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncounterRepository extends TenantCommonRepository<Encounter, Long> {

    /**
     * Busca un Encounter asociado a una Appointment dentro del tenant actual.
     *
     * <p>IMPORTANTE: Este método es multi-tenant safe, evitando accesos a datos
     * de otros tenants.</p>
     *
     * @param appointmentId ID de la cita (Appointment)
     * @param tenantId ID del tenant actual
     * @return Optional con el Encounter si existe, vacío en caso contrario
     */
    Optional<Encounter> findByAppointmentIdAndTenantId(Long appointmentId, Long tenantId);

    /**
     * Verifica si ya existe un encuentro en curso para una cita específica (multi-tenant safe)
     */
    boolean existsByAppointmentIdAndStatusAndTenantId(
            Long appointmentId,
            EncounterStatus status,
            Long tenantId
    );

}
