package com.amachi.app.vitalia.medicalcore.infrastructure.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentUnitRepository extends TenantCommonRepository<DepartmentUnit, Long> {

    Optional<DepartmentUnit> findByCodeAndTenantId(String code, Long tenantId);

    boolean existsByCodeAndTenantId(String code, Long tenantId);

    List<DepartmentUnit> findByParentUnitIdAndTenantId(Long parentUnitId, Long tenantId);

    List<DepartmentUnit> findByParentUnitIsNullAndTenantId(Long tenantId);
}
