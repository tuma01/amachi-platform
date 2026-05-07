package com.amachi.app.vitalia.medicalcore.infrastructure.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.DepartmentUnitDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.DepartmentUnitSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;

import java.util.List;

public interface DepartmentUnitService extends GenericService<DepartmentUnit, DepartmentUnitDto, DepartmentUnitSearchDto> {

    List<DepartmentUnit> getRootUnits();

    List<DepartmentUnit> getSubUnits(Long parentUnitId);
}
