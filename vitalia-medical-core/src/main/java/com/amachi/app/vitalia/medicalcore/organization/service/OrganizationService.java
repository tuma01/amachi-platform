package com.amachi.app.vitalia.medicalcore.organization.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.organization.dto.OrganizationDto;
import com.amachi.app.vitalia.medicalcore.organization.dto.search.OrganizationSearchDto;
import com.amachi.app.vitalia.medicalcore.organization.entity.Organization;

public interface OrganizationService extends GenericService<Organization, OrganizationDto, OrganizationSearchDto> {
}
