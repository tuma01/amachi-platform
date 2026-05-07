package com.amachi.app.vitalia.medicalcore.infrastructure.repository;

import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends TenantCommonRepository<Room, Long> {

    List<Room> findByUnitIdAndTenantId(Long unitId, Long tenantId);

    boolean existsByRoomNumberAndUnitIdAndTenantId(String roomNumber, Long unitId, Long tenantId);
}
