package com.amachi.app.vitalia.medicalcore.medicalhistory.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalHistoryRepository extends TenantCommonRepository<MedicalHistory, Long> {
    Optional<MedicalHistory> findByPatientIdAndIsCurrentTrueAndTenantId(Long patientId, Long tenantId);
    List<MedicalHistory> findByPatientIdAndTenantId(Long patientId, Long tenantId);
    boolean existsByHistoryNumberAndTenantId(String historyNumber, Long tenantId);
}
