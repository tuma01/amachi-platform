package com.amachi.app.vitalia.medicalcore.hospitalization.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.DischargeMedication;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DischargeMedicationRepository extends TenantCommonRepository<DischargeMedication, Long> {

    List<DischargeMedication> findByHospitalizationIdAndTenantId(Long hospitalizationId, Long tenantId);
}
