package com.amachi.app.vitalia.medicalcore.encounter.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.encounter.entity.Condition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends TenantCommonRepository<Condition, Long> {
    List<Condition> findByPatientIdAndTenantId(Long patientId, Long tenantId);
    List<Condition> findByEncounterIdAndTenantId(Long encounterId, Long tenantId);
    boolean existsByEncounterIdAndIcd10IdAndTenantId(
            Long encounterId,
            Long icd10Id,
            Long tenantId
    );
}
