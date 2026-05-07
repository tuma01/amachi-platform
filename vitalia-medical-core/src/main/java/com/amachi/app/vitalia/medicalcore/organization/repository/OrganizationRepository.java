package com.amachi.app.vitalia.medicalcore.organization.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.organization.entity.Organization;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends TenantCommonRepository<Organization, Long> {

    boolean existsByLegalIdentifierAndTenantId(String legalIdentifier, Long tenantId);

    Optional<Organization> findByLegalIdentifierAndTenantId(String legalIdentifier, Long tenantId);
}
