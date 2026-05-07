package com.amachi.app.vitalia.medicalcore.infrastructure.service;

import com.amachi.app.core.common.service.GenericService;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.RoomDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.RoomSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;

import java.util.List;

public interface RoomService extends GenericService<Room, RoomDto, RoomSearchDto> {

    List<Room> getRoomsByUnit(Long unitId);
}
