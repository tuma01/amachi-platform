package com.amachi.app.vitalia.medicalcore.hospitalization.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.common.enums.HospitalizationStatus;
import com.amachi.app.vitalia.medicalcore.hospitalization.entity.Hospitalization;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalizationRepository extends TenantCommonRepository<Hospitalization, Long> {

    List<Hospitalization> findByPatientIdAndTenantId(Long patientId, Long tenantId);

    boolean existsByPatientIdAndStatusAndTenantId(Long patientId, HospitalizationStatus status, Long tenantId);
}
