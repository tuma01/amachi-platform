package com.amachi.app.vitalia.medicalcore.nurse.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NurseRepository extends TenantCommonRepository<Nurse, Long> {

    boolean existsByPersonIdAndTenantId(Long personId, Long tenantId);

    Optional<Nurse> findByPersonIdAndTenantId(Long personId, Long tenantId);

    Optional<Nurse> findByLicenseNumberAndTenantId(String licenseNumber, Long tenantId);
}
