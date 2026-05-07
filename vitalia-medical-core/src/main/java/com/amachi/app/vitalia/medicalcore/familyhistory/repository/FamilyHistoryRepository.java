package com.amachi.app.vitalia.medicalcore.familyhistory.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.FamilyHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyHistoryRepository extends TenantCommonRepository<FamilyHistory, Long> {
    List<FamilyHistory> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
