package com.amachi.app.vitalia.medicalcore.nurse.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.nurse.dto.NurseDto;
import com.amachi.app.vitalia.medicalcore.nurse.dto.search.NurseSearchDto;
import com.amachi.app.vitalia.medicalcore.nurse.entity.Nurse;
import com.amachi.app.vitalia.medicalcore.nurse.mapper.NurseMapper;
import com.amachi.app.vitalia.medicalcore.nurse.repository.NurseRepository;
import com.amachi.app.vitalia.medicalcore.nurse.service.NurseService;
import com.amachi.app.vitalia.medicalcore.nurse.specification.NurseSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NurseServiceImpl extends BaseService<Nurse, NurseDto, NurseSearchDto> implements NurseService {

    private final NurseRepository repository;
    private final NurseMapper mapper;
    private final PersonRepository personRepository;
    private final DepartmentUnitRepository departmentUnitRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Nurse, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<Nurse> buildSpecification(NurseSearchDto searchDto) {
        return new NurseSpecification(searchDto);
    }

    @Override
    @Transactional
    public Nurse create(NurseDto dto) {
        if (dto == null) throw new BusinessException("Nurse data cannot be null");
        Long tenantId = TenantContext.getTenantId();

        Nurse entity = mapper.toEntity(dto);

        // Resolver Person (identidad global)
        if (dto.getPersonId() == null) throw new BusinessException("Person identity is required");
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
        entity.setPerson(person);

        // Unicidad: una persona no puede tener dos perfiles de Nurse en el mismo tenant
        if (repository.existsByPersonIdAndTenantId(dto.getPersonId(), tenantId))
            throw new BusinessException("This person already has a nurse profile in this hospital.");

        // Resolver DepartmentUnit (opcional)
        resolveDepartmentUnit(dto.getDepartmentUnitId(), entity, tenantId);

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(NurseDto dto, Nurse existing) {
        Long currentPersonId = existing.getPerson() != null ? existing.getPerson().getId() : null;
        mapper.updateEntityFromDto(dto, existing);

        if (dto.getPersonId() != null && !dto.getPersonId().equals(currentPersonId)) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
            Long tenantId = TenantContext.getTenantId();
            if (repository.existsByPersonIdAndTenantId(dto.getPersonId(), tenantId))
                throw new BusinessException("This person already has a nurse profile in this hospital.");
            existing.setPerson(person);
        }

        resolveDepartmentUnit(dto.getDepartmentUnitId(), existing, TenantContext.getTenantId());
    }

    private void resolveDepartmentUnit(Long deptUnitId, Nurse nurse, Long tenantId) {
        if (deptUnitId == null) { nurse.setDepartmentUnit(null); return; }
        if (!departmentUnitRepository.existsByIdAndTenantId(deptUnitId, tenantId))
            throw new BusinessException("Unidad hospitalaria no encontrada con ID: " + deptUnitId);
        nurse.setDepartmentUnit(entityManager.getReference(DepartmentUnit.class, deptUnitId));
    }

    @Override
    protected Nurse mapToEntity(NurseDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(Nurse entity) { }

    @Override
    protected void publishUpdatedEvent(Nurse entity) { }
}
