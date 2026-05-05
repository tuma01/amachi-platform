package com.amachi.app.vitalia.medicalcore.doctor.service.impl;

import com.amachi.app.core.auth.context.AuthContextHolder;
import com.amachi.app.core.auth.identity.service.IdentityResolutionService;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.vitalia.medicalcore.doctor.dto.DoctorDto;
import com.amachi.app.vitalia.medicalcore.doctor.dto.search.DoctorSearchDto;
import com.amachi.app.vitalia.medicalcore.doctor.entity.Doctor;
import com.amachi.app.vitalia.medicalcore.doctor.event.DoctorCreatedEvent;
import com.amachi.app.vitalia.medicalcore.doctor.event.DoctorUpdatedEvent;
import com.amachi.app.vitalia.medicalcore.doctor.mapper.DoctorMapper;
import com.amachi.app.vitalia.medicalcore.doctor.repository.DoctorRepository;
import com.amachi.app.vitalia.medicalcore.doctor.service.DoctorService;
import com.amachi.app.vitalia.medicalcore.doctor.specification.DoctorSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Standard implementation of Doctor Service using BaseService (SaaS Elite Tier).
 * Patrón validado: DTO-First con hooks de mapeo y publicación de eventos.
 */
@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorServiceImpl extends BaseService<Doctor, DoctorDto, DoctorSearchDto> implements DoctorService {

    private final DoctorRepository repository;
    private final DoctorMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final PersonRepository personRepository;

    @Override
    protected CommonRepository<Doctor, Long> getRepository() {
        return repository;
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    @Transactional
    public Doctor create(DoctorDto dto) {
        if (dto == null) throw new BusinessException("Doctor data cannot be null");

        // 1. Mapear DTO a Entidad
        Doctor entity = mapToEntity(dto);

        // 2. Manejo de Identidad (Simple/Local para Fase 1)
        if (entity.getPerson() == null || entity.getPerson().getId() == null) {
            throw new BusinessException("Doctor identity (Person) is required");
        }

        Long personId = entity.getPerson().getId();

        Person managedPerson = personRepository.findById(personId)
                .orElseThrow(() -> new BusinessException("Person not found with id: " + personId));

        entity.setPerson(managedPerson);

        // 3. Obtener tenant desde contexto (fuente única)
        Long tenantId = TenantContext.getTenantId();

        // 4. Validación de Unicidad Local
        if (repository.existsByPersonIdAndTenantId(entity.getPerson().getId(), tenantId)) {
            throw new BusinessException("This person already has a doctor profile in this hospital.");
        }

        // 5. Persistencia
        Doctor saved = repository.save(entity);
        
        // 6. Publicación de Evento
        publishCreatedEvent(saved);
        return saved;
    }

    @Override
    protected Specification<Doctor> buildSpecification(DoctorSearchDto searchDto) {
        return new DoctorSpecification(searchDto);
    }

    @Override
    protected Doctor mapToEntity(DoctorDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(DoctorDto dto, Doctor existing) {
        // 1. Snapshot de Person actual
        Long currentPersonId = existing.getPerson() != null ? existing.getPerson().getId() : null;

        // 2. Update parcial (mapper)
        mapper.updateEntityFromDto(dto, existing);

        // 3. Resolver cambio de Person
        if (dto.getPerson() != null && dto.getPerson().getId() != null) {

            Long newPersonId = dto.getPerson().getId();

            // solo si cambió
            if (!newPersonId.equals(currentPersonId)) {

                Person managedPerson = personRepository.findById(newPersonId)
                        .orElseThrow(() -> new BusinessException("Person not found with id: " + newPersonId));

                Long tenantId = TenantContext.getTenantId();

                // validación de unicidad
                if (repository.existsByPersonIdAndTenantId(newPersonId, tenantId)) {
                    throw new BusinessException("This person already has a doctor profile in this hospital.");
                }

                // aplicar cambio seguro
                existing.setPerson(managedPerson);
            }
        }
    }

    @Override
    protected void publishCreatedEvent(Doctor entity) {
        eventPublisher.publish(new DoctorCreatedEvent(
                entity.getId(),
                entity.getExternalId(),
                entity.getLicenseNumber(),
                entity.getPerson() != null ? entity.getPerson().getId() : null
        ));
    }

    @Override
    protected void publishUpdatedEvent(Doctor entity) {
        eventPublisher.publish(new DoctorUpdatedEvent(
                entity.getId(),
                entity.getLicenseNumber(),
                entity.isActive()
        ));
    }
}
