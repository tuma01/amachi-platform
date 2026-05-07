package com.amachi.app.vitalia.medicalcore.habit.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.habit.entity.ToxicHabit;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToxicHabitRepository extends TenantCommonRepository<ToxicHabit, Long> {
    List<ToxicHabit> findByMedicalHistoryIdAndTenantId(Long medicalHistoryId, Long tenantId);
}
