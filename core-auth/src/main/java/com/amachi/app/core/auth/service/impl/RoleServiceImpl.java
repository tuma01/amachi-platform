package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.dto.RoleDto;
import com.amachi.app.core.auth.dto.search.RoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.event.RoleCreatedEvent;
import com.amachi.app.core.auth.repository.RoleRepository;
import com.amachi.app.core.auth.service.RoleService;
import com.amachi.app.core.auth.specification.RoleSpecification;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl extends BaseService<Role, RoleDto, RoleSearchDto> implements RoleService {

    private final RoleRepository repository;
    private final DomainEventPublisher eventPublisher;

    @Override
    protected CommonRepository<Role, Long> getRepository() {
        return repository;
    }

    @Override
    protected Specification<Role> buildSpecification(RoleSearchDto searchDto) {
        return new RoleSpecification(searchDto);
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    protected void publishCreatedEvent(Role entity) {
        eventPublisher.publish(new RoleCreatedEvent(entity.getId(), entity.getName()));
    }

    @Override
    protected void publishUpdatedEvent(Role entity) {
        // Roles are global catalogs; an update event is not required in the current
        // event-driven flow. Method must be implemented to satisfy BaseService contract.
        log.debug("Role updated: id={}, name={}", entity.getId(), entity.getName());
    }

    @Override
    protected Role mapToEntity(RoleDto dto) {
        return Role.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    @Override
    protected void mergeEntities(RoleDto dto, Role existing) {
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findByNames(List<String> names) {
        return repository.findByNameIn(names);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "error.role_not_found", name));
    }
}
