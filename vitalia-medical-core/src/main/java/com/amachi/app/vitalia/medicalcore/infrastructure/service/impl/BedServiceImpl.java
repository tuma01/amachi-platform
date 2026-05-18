package com.amachi.app.vitalia.medicalcore.infrastructure.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.BedDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.BedSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Bed;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.Room;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.BedMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.BedRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.RoomRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.BedService;
import com.amachi.app.vitalia.medicalcore.infrastructure.specification.BedSpecification;
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
public class BedServiceImpl
        extends BaseService<Bed, BedDto, BedSearchDto>
        implements BedService {

    private final BedRepository repository;
    private final BedMapper mapper;
    private final RoomRepository roomRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Bed create(BedDto dto) {
        Long tenantId = TenantContext.getTenantId();

        if (!roomRepository.existsByIdAndTenantId(dto.getRoomId(), tenantId)) {
            throw new BusinessException("Habitación no encontrada con ID: " + dto.getRoomId());
        }

        if (repository.existsByBedCodeAndRoomIdAndTenantId(
                dto.getBedCode().trim().toUpperCase(), dto.getRoomId(), tenantId)) {
            throw new BusinessException("Ya existe la cama con código '" + dto.getBedCode() + "' en esta habitación");
        }

        Bed bed = mapper.toEntity(dto);
        bed.setIsOccupied(false);
        if (bed.getActive() == null) bed.setActive(true);
        bed.setRoom(entityManager.getReference(Room.class, dto.getRoomId()));
        return repository.save(bed);
    }

    @Override
    @Transactional
    public Bed update(Long id, BedDto dto) {
        Bed existing = getById(id);
        mapper.updateEntityFromDto(dto, existing);
        if (dto.getRoomId() != null && !dto.getRoomId().equals(existing.getRoom().getId())) {
            if (!roomRepository.existsByIdAndTenantId(dto.getRoomId(), TenantContext.getTenantId())) {
                throw new BusinessException("Habitación no encontrada con ID: " + dto.getRoomId());
            }
            existing.setRoom(entityManager.getReference(Room.class, dto.getRoomId()));
        }
        return repository.save(existing);
    }

    @Override
    public List<Bed> getBedsByRoom(Long roomId) {
        return repository.findByRoomIdAndTenantId(roomId, TenantContext.getTenantId());
    }

    @Override
    @Transactional
    public Bed updateStatus(Long id, BedStatusEnum status) {
        Bed bed = getById(id);
        bed.setStatus(status);
        bed.setIsOccupied(BedStatusEnum.OCCUPIED.equals(status));
        return repository.save(bed);
    }

    @Override
    protected CommonRepository<Bed, Long> getRepository() { return repository; }

    @Override
    protected Specification<Bed> buildSpecification(BedSearchDto s) { return new BedSpecification(s); }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(Bed entity) {}

    @Override
    protected void publishUpdatedEvent(Bed entity) {}

    @Override
    protected Bed mapToEntity(BedDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(BedDto dto, Bed existing) { mapper.updateEntityFromDto(dto, existing); }
}
