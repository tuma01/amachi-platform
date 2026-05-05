package com.amachi.app.core.auth.identity.service.impl;

import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.auth.identity.service.IdentityResolutionService;
import com.amachi.app.core.domain.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación robusta de resolución de identidad (SaaS Elite Tier).
 * Maneja condiciones de carrera e integridad de datos de forma idempotente.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityResolutionServiceImpl implements IdentityResolutionService {

    private final PersonRepository personRepository;

    @Override
    @Transactional
    public Person resolveOrCreateIdentity(String nationalId, String email, Person personData) {
        log.info("[IDENTITY] Resolving identity for nationalId: {}, email: {}", nationalId, email);

        // 1. Prioridad: Búsqueda por NationalId (Ancla universal)
        if (nationalId != null && !nationalId.isBlank()) {
            return personRepository.findByNationalId(nationalId)
                    .map(person -> {
                        log.debug("[IDENTITY] Person found by nationalId (ID: {})", person.getId());
                        return person;
                    })
                    .orElseGet(() -> createWithIdempotency(nationalId, email, personData));
        }

        // 2. Fallback: Búsqueda por Email (Ancla de comunicación)
        if (email != null && !email.isBlank()) {
            return personRepository.findByEmail(email)
                    .map(person -> {
                        log.debug("[IDENTITY] Person found by email (ID: {})", person.getId());
                        return person;
                    })
                    .orElseGet(() -> createWithIdempotency(nationalId, email, personData));
        }

        // 3. Creación directa si no hay anclas (No recomendado, pero soportado)
        return createWithIdempotency(nationalId, email, personData);
    }

    /**
     * Intenta persistir la identidad manejando violaciones de integridad por concurrencia.
     */
    private Person createWithIdempotency(String nationalId, String email, Person personData) {
        try {
            log.info("[IDENTITY] No identity found. Creating new Person record.");
            Person newPerson = personData != null ? personData : new Person();
            if (nationalId != null) newPerson.setNationalId(nationalId);
            if (email != null) newPerson.setEmail(email);
            
            return personRepository.save(newPerson);
        } catch (DataIntegrityViolationException e) {
            log.warn("[IDENTITY] Race condition detected. Re-resolving identity.");
            
            // Re-intento estratégico después de fallo por concurrencia
            if (nationalId != null) {
                return personRepository.findByNationalId(nationalId).orElseThrow();
            }
            if (email != null) {
                return personRepository.findByEmail(email).orElseThrow();
            }
            throw e;
        }
    }
}
