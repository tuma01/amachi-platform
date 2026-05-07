package com.amachi.app.vitalia.medicalcore.employee.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.employee.dto.EmployeeDto;
import com.amachi.app.vitalia.medicalcore.employee.dto.search.EmployeeSearchDto;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;

public interface EmployeeService extends GenericService<Employee, EmployeeDto, EmployeeSearchDto> {
}
