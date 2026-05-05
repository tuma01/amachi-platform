package com.amachi.app.vitalia.medicalcore.prescription.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.prescription.entity.Prescription;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends TenantCommonRepository<Prescription, Long> {
    List<Prescription> findByEncounterIdAndTenantId(Long encounterId, Long tenantId);
    List<Prescription> findByPatientIdAndTenantId(Long patientId, Long tenantId);
}
