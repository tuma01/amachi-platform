package com.amachi.app.vitalia.medicalcore.infrastructure.repository;

import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.repository.TenantCommonRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedRepository extends TenantCommonRepository<Bed, Long> {

    List<Bed> findByRoomIdAndTenantId(Long roomId, Long tenantId);

    List<Bed> findByStatusAndTenantId(BedStatusEnum status, Long tenantId);

    boolean existsByBedCodeAndRoomIdAndTenantId(String bedCode, Long roomId, Long tenantId);

    long countByRoomIdAndIsOccupiedAndTenantId(Long roomId, Boolean isOccupied, Long tenantId);
}
