package com.amachi.app.core.domain.repository;

import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.entity.PersonTenant;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PersonTenantRepository
        extends TenantCommonRepository<PersonTenant, Long> {
    Page<PersonTenant> findByTenantId(Long tenantId, Pageable pageable);

    Optional<PersonTenant> findByPersonIdAndTenantId(Long personId, Long tenantId);
    Optional<PersonTenant> findByPersonIdAndTenantCode(Long personId, String tenantCode);

    Optional<PersonTenant> findByPersonAndTenant(Person person, Tenant tenant);

    Optional<PersonTenant> findByPersonAndTenantAndRoleContext(Person person, Tenant tenant, RoleContext roleContext);

    Optional<PersonTenant> findByNationalHealthIdAndTenant(String nationalHealthId, Tenant tenant);

    boolean existsByPersonAndTenant(Person person, Tenant tenant);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonTenant pt WHERE pt.person = :person AND pt.tenant = :tenant")
    void deleteByPersonAndTenant(@Param("person") Person person, @Param("tenant") Tenant tenant);

    @Query("SELECT COUNT(pt) > 0 FROM PersonTenant pt WHERE pt.person.id = :personId")
    boolean existsByPersonId(@Param("personId") Long personId);

    @Query("SELECT COUNT(pt) FROM PersonTenant pt WHERE pt.person.id = :personId")
    long countByPersonId(@Param("personId") Long personId);

    boolean existsByTenantAndRoleContext(Tenant tenant, RoleContext roleContext);
}
