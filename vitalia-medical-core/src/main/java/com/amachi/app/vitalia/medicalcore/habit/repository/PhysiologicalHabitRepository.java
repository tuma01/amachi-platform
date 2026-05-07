package com.amachi.app.vitalia.medicalcore.habit.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.habit.entity.PhysiologicalHabit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhysiologicalHabitRepository extends TenantCommonRepository<PhysiologicalHabit, Long> {
    List<PhysiologicalHabit> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
