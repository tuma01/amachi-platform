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
import com.amachi.app.vitalia.medicalcatalog.bloodtype.repository.BloodTypeRepository;
import com.amachi.app.core.domain.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
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
    private final PersonMapper personMapper;
    private final DomainEventPublisher eventPublisher;
    private final PersonRepository personRepository;
    private final BloodTypeRepository bloodTypeRepository;

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
        resolveBloodType(entity, dto.getBloodTypeId());

        // 2. Resolver Person — Protocolo de Resolución de Identidad (sección 23.D del guide)
        Person managedPerson = resolveOrCreatePerson(dto);
        entity.setPerson(managedPerson);

        // 3. Validación de unicidad por tenant
        Long tenantId = TenantContext.getTenantId();
        if (repository.existsByPersonIdAndTenantId(managedPerson.getId(), tenantId)) {
            throw new BusinessException("A patient with this identity already exists in this hospital.");
        }

        // 4. Persistencia (tenant se asigna en @PrePersist)
        Patient saved = repository.save(entity);
        publishCreatedEvent(saved);
        return saved;
    }

    /**
     * Protocolo de Resolución de Identidad:
     * 1. Si llega person.id → buscar persona existente por ID.
     * 2. Si llega person.nationalId → buscar por cédula/documento para evitar duplicados.
     * 3. Si no existe → crear nueva Person con los datos del formulario.
     */
    private Person resolveOrCreatePerson(PatientDto dto) {
        if (dto.getPerson() == null) {
            throw new BusinessException("Patient identity (Person) is required");
        }

        if (dto.getPerson().getId() != null) {
            return personRepository.findById(dto.getPerson().getId())
                    .orElseThrow(() -> new BusinessException("Person not found with id: " + dto.getPerson().getId()));
        }

        if (dto.getPerson().getNationalId() != null) {
            Optional<Person> existing = personRepository.findByNationalId(dto.getPerson().getNationalId());
            if (existing.isPresent()) {
                return existing.get();
            }
        }

        return personRepository.save(personMapper.toEntity(dto.getPerson()));
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

        // 1. Aplicar update parcial de campos clínicos del paciente
        mapper.updateEntityFromDto(dto, existing);
        resolveBloodType(existing, dto.getBloodTypeId());

        // 2. Actualizar datos de la Person vinculada (nombre, email, teléfono, etc.)
        if (dto.getPerson() != null && existing.getPerson() != null) {
            personMapper.updateEntityFromDto(dto.getPerson(), existing.getPerson());
        }
    }

    /**
     * Reemplaza el objeto BloodType transient generado por MapStruct con una referencia
     * JPA gestionada obtenida vía getReferenceById(). Esto evita TransientObjectException
     * cuando Hibernate intenta hacer flush de la asociación @ManyToOne.
     */
    private void resolveBloodType(Patient patient, Long bloodTypeId) {
        if (patient.getDetails() == null) return;
        if (bloodTypeId != null) {
            patient.getDetails().setBloodType(bloodTypeRepository.getReferenceById(bloodTypeId));
        } else {
            patient.getDetails().setBloodType(null);
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
