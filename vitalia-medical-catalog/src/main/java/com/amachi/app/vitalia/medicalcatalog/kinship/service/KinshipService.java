package com.amachi.app.vitalia.medicalcatalog.kinship.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.KinshipDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.dto.search.KinshipSearchDto;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;

public interface KinshipService extends GenericService<Kinship, KinshipDto, KinshipSearchDto> {
}
