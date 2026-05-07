package com.amachi.app.vitalia.medicalcore.infrastructure.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import com.amachi.app.vitalia.medicalcore.employee.repository.EmployeeRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.DepartmentUnitDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.dto.search.DepartmentUnitSearchDto;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.mapper.DepartmentUnitMapper;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
import com.amachi.app.vitalia.medicalcore.infrastructure.service.DepartmentUnitService;
import com.amachi.app.vitalia.medicalcore.infrastructure.specification.DepartmentUnitSpecification;
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
public class DepartmentUnitServiceImpl
        extends BaseService<DepartmentUnit, DepartmentUnitDto, DepartmentUnitSearchDto>
        implements DepartmentUnitService {

    private final DepartmentUnitRepository repository;
    private final DepartmentUnitMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final EmployeeRepository employeeRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public DepartmentUnit create(DepartmentUnitDto dto) {
        Long tenantId = TenantContext.getTenantId();

        if (repository.existsByCodeAndTenantId(dto.getCode().trim().toUpperCase(), tenantId)) {
            throw new BusinessException("Ya existe una unidad con el código: " + dto.getCode());
        }

        DepartmentUnit unit = mapper.toEntity(dto);
        resolveParentUnit(dto.getParentUnitId(), unit, tenantId);
        resolveUnitHead(dto.getUnitHeadId(), unit, tenantId);
        return repository.save(unit);
    }

    @Override
    @Transactional
    public DepartmentUnit update(Long id, DepartmentUnitDto dto) {
        DepartmentUnit existing = getById(id);
        mapper.updateEntityFromDto(dto, existing);
        Long tenantId = TenantContext.getTenantId();
        resolveParentUnit(dto.getParentUnitId(), existing, tenantId);
        resolveUnitHead(dto.getUnitHeadId(), existing, tenantId);
        return repository.save(existing);
    }

    @Override
    public List<DepartmentUnit> getRootUnits() {
        return repository.findByParentUnitIsNullAndTenantId(TenantContext.getTenantId());
    }

    @Override
    public List<DepartmentUnit> getSubUnits(Long parentUnitId) {
        return repository.findByParentUnitIdAndTenantId(parentUnitId, TenantContext.getTenantId());
    }

    private void resolveUnitHead(Long unitHeadId, DepartmentUnit unit, Long tenantId) {
        if (unitHeadId == null) {
            unit.setUnitHead(null);
            return;
        }
        if (!employeeRepository.existsByIdAndTenantId(unitHeadId, tenantId)) {
            throw new BusinessException("Empleado (jefe de unidad) no encontrado con ID: " + unitHeadId);
        }
        unit.setUnitHead(entityManager.getReference(Employee.class, unitHeadId));
    }

    private void resolveParentUnit(Long parentUnitId, DepartmentUnit unit, Long tenantId) {
        if (parentUnitId == null) {
            unit.setParentUnit(null);
            return;
        }
        if (!repository.existsByIdAndTenantId(parentUnitId, tenantId)) {
            throw new BusinessException("Unidad padre no encontrada con ID: " + parentUnitId);
        }
        unit.setParentUnit(entityManager.getReference(DepartmentUnit.class, parentUnitId));
    }

    @Override
    protected CommonRepository<DepartmentUnit, Long> getRepository() { return repository; }

    @Override
    protected Specification<DepartmentUnit> buildSpecification(DepartmentUnitSearchDto s) {
        return new DepartmentUnitSpecification(s);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected void publishCreatedEvent(DepartmentUnit entity) {}

    @Override
    protected void publishUpdatedEvent(DepartmentUnit entity) {}

    @Override
    protected DepartmentUnit mapToEntity(DepartmentUnitDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void mergeEntities(DepartmentUnitDto dto, DepartmentUnit existing) {
        Long tenantId = TenantContext.getTenantId();
        mapper.updateEntityFromDto(dto, existing);
        resolveParentUnit(dto.getParentUnitId(), existing, tenantId);
        resolveUnitHead(dto.getUnitHeadId(), existing, tenantId);
    }
}
