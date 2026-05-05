package com.amachi.app.vitalia.medicalcore.observation.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.observation.entity.Observation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationRepository extends TenantCommonRepository<Observation, Long> {
    List<Observation> findByEncounterIdAndTenantId(Long encounterId, Long tenantId);
    List<Observation> findByPatientIdAndTenantId(Long patientId, Long tenantId);
}
