package com.amachi.app.vitalia.medicalcore.medicationdispense.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.medicationdispense.entity.MedicationDispense;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationDispenseRepository extends TenantCommonRepository<MedicationDispense, Long> {

    List<MedicationDispense> findByPatientIdAndTenantId(Long patientId, Long tenantId);

    List<MedicationDispense> findByPrescriptionIdAndTenantId(Long prescriptionId, Long tenantId);
}
