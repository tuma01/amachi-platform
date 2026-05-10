package com.amachi.app.vitalia.medicalcore.medicationadministration.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.medicationadministration.entity.MedicationAdministration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationAdministrationRepository extends TenantCommonRepository<MedicationAdministration, Long> {

    List<MedicationAdministration> findByPatientIdAndTenantId(Long patientId, Long tenantId);

    List<MedicationAdministration> findByPrescriptionIdAndTenantId(Long prescriptionId, Long tenantId);
}
