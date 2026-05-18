package com.amachi.app.vitalia.medicalcore.insurance.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.insurance.entity.Insurance;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends TenantCommonRepository<Insurance, Long> {
    List<Insurance> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
    List<Insurance> findByMedicalHistoryIdAndIsCurrentTrueAndTenantId(Long medicalHistoryId, Long tenantId);
    List<Insurance> findByMedicalHistoryPatientIdAndTenantId(Long patientId, Long tenantId);
}
