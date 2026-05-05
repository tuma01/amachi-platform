package com.amachi.app.vitalia.medicalcatalog.identity.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.IdentificationTypeDto;
import com.amachi.app.vitalia.medicalcatalog.identity.dto.search.IdentificationTypeSearchDto;
import com.amachi.app.vitalia.medicalcatalog.identity.entity.IdentificationType;

public interface IdentificationTypeService extends GenericService<IdentificationType, IdentificationTypeDto, IdentificationTypeSearchDto> {
}
