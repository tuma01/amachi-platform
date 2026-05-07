package com.amachi.app.vitalia.medicalcore.professional.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.professional.entity.ProfessionalInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessionalInfoRepository extends TenantCommonRepository<ProfessionalInfo, Long> {

    List<ProfessionalInfo> findByPersonIdAndTenantId(Long personId, Long tenantId);

    List<ProfessionalInfo> findByPersonIdAndIsCurrentTrueAndTenantId(Long personId, Long tenantId);
}
