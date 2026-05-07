package com.amachi.app.vitalia.medicalcore.infrastructure.service;

import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.BedDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.BedSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;

import java.util.List;

public interface BedService extends GenericService<Bed, BedDto, BedSearchDto> {

    List<Bed> getBedsByRoom(Long roomId);

    Bed updateStatus(Long id, BedStatusEnum status);
}
