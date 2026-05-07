package com.amachi.app.vitalia.medicalcore.familyhistory.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.familyhistory.entity.HereditaryDisease;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HereditaryDiseaseRepository extends TenantCommonRepository<HereditaryDisease, Long> {
    List<HereditaryDisease> findByFamilyHistoryIdAndTenantId(Long familyHistoryId, Long tenantId);
}
