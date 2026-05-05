package com.amachi.app.vitalia.medicalcore.patient.service.impl;

import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.BusinessException;
import com.amachi.app.core.common.repository.CommonRepository;
import com.amachi.app.core.common.service.BaseService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonRepository;
import com.amachi.app.vitalia.medicalcore.patient.dto.PatientDto;
import com.amachi.app.vitalia.medicalcore.patient.dto.search.PatientSearchDto;
import com.amachi.app.vitalia.medicalcore.patient.entity.Patient;
import com.amachi.app.vitalia.medicalcore.patient.event.PatientCreatedEvent;
import com.amachi.app.vitalia.medicalcore.patient.event.PatientUpdatedEvent;
import com.amachi.app.vitalia.medicalcore.patient.mapper.PatientMapper;
import com.amachi.app.vitalia.medicalcore.patient.repository.PatientRepository;
import com.amachi.app.vitalia.medicalcore.patient.service.PatientService;
import com.amachi.app.vitalia.medicalcore.patient.specification.PatientSpecification;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Enterprise implementation of Patient Service (SaaS Elite Tier).
 * Patrón validado: DTO-First con hooks de mapeo y publicación de eventos.
 */
@Service
@TenantAware
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl extends BaseService<Patient, PatientDto, PatientSearchDto> implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;
    private final DomainEventPublisher eventPublisher;
    private final PersonRepository personRepository;

    @Override
    protected CommonRepository<Patient, Long> getRepository() {
        return repository;
    }

    @Override
    protected DomainEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    @Override
    @Transactional
    public Patient create(PatientDto dto) {
        if (dto == null) throw new BusinessException("Patient data cannot be null");
        
        // 1. Mapear DTO a Entidad
        Patient entity = mapToEntity(dto);

        // 2. Manejo de Identidad (Simple/Local para Fase 1)
        // Vinculamos la Person directamente sin forzar resolución global atómica aún.
        if (entity.getPerson() == null || entity.getPerson().getId() == null) {
            throw new BusinessException("Patient identity (Person) is required");
        }

        Long personId = entity.getPerson().getId();

        Person managedPerson = personRepository.findById(personId)
                .orElseThrow(() -> new BusinessException("Person not found with id: " + personId));

        entity.setPerson(managedPerson);

        // 3. Obtener tenant desde contexto (fuente única)
        Long tenantId = TenantContext.getTenantId();

        // 4. Validación de Unicidad Local
        if (repository.existsByPersonIdAndTenantId(entity.getPerson().getId(), tenantId)) {
            throw new BusinessException("A patient with this identity already exists in this hospital.");
        }

        // 5. Persistencia (tenant se asigna en @PrePersist)
        Patient saved = repository.save(entity);
        
        // 6. Publicación de Evento (Opcional en esta fase, pero mantenida por compatibilidad)
        publishCreatedEvent(saved);
        return saved;
    }

    @Override
    protected Specification<Patient> buildSpecification(PatientSearchDto searchDto) {
        return new PatientSpecification(searchDto);
    }

    @Override
    protected Patient mapToEntity(PatientDto dto) {
        return mapper.toEntity(dto);
    }

    @Override
    protected void mergeEntities(PatientDto dto, Patient existing) {

        // 1. Guardar person actual
        Long currentPersonId = existing.getPerson() != null ? existing.getPerson().getId() : null;

        // 2. Aplicar update parcial
        mapper.updateEntityFromDto(dto, existing);

        // 3. Resolver person desde DTO
        if (dto.getPerson() != null && dto.getPerson().getId() != null) {
            Long newPersonId = dto.getPerson().getId();

            // solo si cambió
            if (!newPersonId.equals(currentPersonId)) {
                Person managedPerson = personRepository.findById(newPersonId)
                        .orElseThrow(() -> new BusinessException("Person not found with id: " + newPersonId));

                Long tenantId = TenantContext.getTenantId();
                if (repository.existsByPersonIdAndTenantId(newPersonId, tenantId)) {
                    throw new BusinessException("A patient with this identity already exists in this hospital.");
                }
                existing.setPerson(managedPerson);
            }
        }
    }

    @Override
    protected void publishCreatedEvent(Patient entity) {
        eventPublisher.publish(new PatientCreatedEvent(
                entity.getId(),
                entity.getIdentificationNumber(),
                entity.getPerson() != null ? entity.getPerson().getId() : null
        ));
    }

    @Override
    protected void publishUpdatedEvent(Patient entity) {
        eventPublisher.publish(new PatientUpdatedEvent(
                entity.getId(),
                entity.getIdentificationNumber()
        ));
    }
}
