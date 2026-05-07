package com.amachi.app.vitalia.medicalcore.employee.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends TenantCommonRepository<Employee, Long> {

    boolean existsByPersonIdAndTenantId(Long personId, Long tenantId);

    Optional<Employee> findByPersonIdAndTenantId(Long personId, Long tenantId);

    Optional<Employee> findByEmployeeCodeAndTenantId(String employeeCode, Long tenantId);
}
