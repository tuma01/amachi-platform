package com.amachi.app.vitalia.medicalcore.medicalhistory.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.ActualIllness;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActualIllnessRepository extends TenantCommonRepository<ActualIllness, Long> {
    List<ActualIllness> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
