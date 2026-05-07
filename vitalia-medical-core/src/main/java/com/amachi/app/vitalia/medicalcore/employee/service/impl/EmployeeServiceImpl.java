package com.amachi.app.vitalia.medicalcore.employee.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.vitalia.medicalcore.employee.dto.EmployeeDto;
import com.amachi.app.vitalia.medicalcore.employee.dto.search.EmployeeSearchDto;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import com.amachi.app.vitalia.medicalcore.employee.mapper.EmployeeMapper;
import com.amachi.app.vitalia.medicalcore.employee.repository.EmployeeRepository;
import com.amachi.app.vitalia.medicalcore.employee.service.EmployeeService;
import com.amachi.app.vitalia.medicalcore.employee.specification.EmployeeSpecification;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.infrastructure.repository.DepartmentUnitRepository;
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
public class EmployeeServiceImpl extends BaseService<Employee, EmployeeDto, EmployeeSearchDto> implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final PersonRepository personRepository;
    private final DepartmentUnitRepository departmentUnitRepository;
    private final DomainEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    protected CommonRepository<Employee, Long> getRepository() { return repository; }

    @Override
    protected DomainEventPublisher getEventPublisher() { return eventPublisher; }

    @Override
    protected Specification<Employee> buildSpecification(EmployeeSearchDto searchDto) {
        return new EmployeeSpecification(searchDto);
    }

    @Override
    @Transactional
    public Employee create(EmployeeDto dto) {
        if (dto == null) throw new BusinessException("Employee data cannot be null");
        Long tenantId = TenantContext.getTenantId();

        Employee entity = mapper.toEntity(dto);

        // Resolver Person (identidad global)
        if (dto.getPersonId() == null) throw new BusinessException("Person identity is required");
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
        entity.setPerson(person);

        // Unicidad: una persona no puede tener dos perfiles de Employee en el mismo tenant
        if (repository.existsByPersonIdAndTenantId(dto.getPersonId(), tenantId))
            throw new BusinessException("This person already has an employee profile in this hospital.");

        // Resolver DepartmentUnit (opcional)
        resolveDepartmentUnit(dto.getDepartmentUnitId(), entity, tenantId);

        return repository.save(entity);
    }

    @Override
    protected void mergeEntities(EmployeeDto dto, Employee existing) {
        Long currentPersonId = existing.getPerson() != null ? existing.getPerson().getId() : null;
        mapper.updateEntityFromDto(dto, existing);

        if (dto.getPersonId() != null && !dto.getPersonId().equals(currentPersonId)) {
            Person person = personRepository.findById(dto.getPersonId())
                    .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPersonId()));
            Long tenantId = TenantContext.getTenantId();
            if (repository.existsByPersonIdAndTenantId(dto.getPersonId(), tenantId))
                throw new BusinessException("This person already has an employee profile in this hospital.");
            existing.setPerson(person);
        }

        resolveDepartmentUnit(dto.getDepartmentUnitId(), existing, TenantContext.getTenantId());
    }

    private void resolveDepartmentUnit(Long deptUnitId, Employee employee, Long tenantId) {
        if (deptUnitId == null) { employee.setDepartmentUnit(null); return; }
        if (!departmentUnitRepository.existsByIdAndTenantId(deptUnitId, tenantId))
            throw new BusinessException("Unidad hospitalaria no encontrada con ID: " + deptUnitId);
        employee.setDepartmentUnit(entityManager.getReference(DepartmentUnit.class, deptUnitId));
    }

    @Override
    protected Employee mapToEntity(EmployeeDto dto) { return mapper.toEntity(dto); }

    @Override
    protected void publishCreatedEvent(Employee entity) { }

    @Override
    protected void publishUpdatedEvent(Employee entity) { }
}
