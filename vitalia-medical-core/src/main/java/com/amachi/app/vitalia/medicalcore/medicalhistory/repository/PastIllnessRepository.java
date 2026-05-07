package com.amachi.app.vitalia.medicalcore.medicalhistory.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.PastIllness;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PastIllnessRepository extends TenantCommonRepository<PastIllness, Long> {
    List<PastIllness> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
