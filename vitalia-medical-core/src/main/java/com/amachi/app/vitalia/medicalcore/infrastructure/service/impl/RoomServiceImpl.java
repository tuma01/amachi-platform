package com.amachi.app.vitalia.medicalcore.infrastructure.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.RoomDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.RoomSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.RoomMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.RoomRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.RoomService;
import com.amachi.app.vitalia.medicalcore.infrastructure.specification.RoomSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl
        extends BaseService<Room, RoomDto, RoomSearchDto>
        implements RoomService {

    private final RoomRepository repository;
    private final RoomMapper mapper;
    private final DepartmentUnitRepository unitRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Room create(RoomDto dto) {
        Long tenantId = TenantContext.getTenantId();

        if (!unitRepository.existsByIdAndTenantId(dto.getUnitId(), tenantId)) {
            throw new BusinessException("Unidad hospitalaria no encontrada con ID: " + dto.getUnitId());
        }

        if (repository.existsByRoomNumberAndUnitIdAndTenantId(
                dto.getRoomNumber().trim().toUpperCase(), dto.getUnitId(), tenantId)) {
            throw new BusinessException("Ya existe la habitación '" + dto.getRoomNumber() + "' en esta unidad");
        }

        Room room = mapper.toEntity(dto);
        room.setUnit(entityManager.getReference(DepartmentUnit.class, dto.getUnitId()));
        return repository.save(room);
    }

    @Override
    @Transactional
    public Room update(Long id, RoomDto dto) {
        Room existing = getById(id);
        mapper.updateEntityFromDto(dto, existing);
        if (dto.getUnitId() != null && !dto.getUnitId().equals(existing.getUnit().getId())) {
            if (!unitRepository.existsByIdAndTenantId(dto.getUnitId(), TenantContext.getTenantId())) {
                throw new BusinessException("Unidad hospitalaria no encontrada con ID: " + dto.getUnitId());
            }
            existing.setUnit(entityManager.getReference(DepartmentUnit.class, dto.getUnitId()));
        }
        return repository.save(existing);
    }

    @Override
    public List<Room> getRoomsByUnit(Long unitId) {
        return repository.findByUnitIdAndTenantId(unitId, TenantContext.getTenantId());
    }

    @Override
    protected CommonRepository<Room, Long> getRepository() { return repository; }

    @Override
    protected Specification<Room> buildSpecification(RoomSearchDto s) { return new RoomSpecification(s); }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Room entity) {}

    @Override
    protected void publishUpdatedEvent(Room entity) {}

    @Override
    protected Room mapToEntity(RoomDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(RoomDto dto, Room existing) { mapper.updateEntityFromDto(dto, existing); }
}
