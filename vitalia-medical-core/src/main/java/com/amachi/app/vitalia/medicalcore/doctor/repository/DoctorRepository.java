package com.amachi.app.vitalia.medicalcore.doctor.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for medical doctor management.
 * Extends CommonRepository for automatic multi-tenant isolation.
 */
@Repository
public interface DoctorRepository extends TenantCommonRepository<Doctor, Long> {

    /**
     * Find a doctor by their professional license number within a specific tenant.
     */
    Optional<Doctor> findByLicenseNumberAndTenantId(String licenseNumber, Long tenantId);

    /**
     * Find a doctor by their billing provider number within a specific tenant.
     */
    Optional<Doctor> findByProviderNumberAndTenantId(String providerNumber, Long tenantId);

    /**
     * Identity resolution: verify if a Person already has an active Doctor role in this Tenant.
     * MANDATORY before creating a Doctor to prevent duplicate domain contexts.
     */
    boolean existsByPersonIdAndTenantId(Long personId, Long tenantId);

    /**
     * Identity resolution: find an existing Doctor by Person + Tenant.
     */
    Optional<Doctor> findByPersonIdAndTenantId(Long personId, Long tenantId);
}

