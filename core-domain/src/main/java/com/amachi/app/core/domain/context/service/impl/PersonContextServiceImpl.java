package com.amachi.app.core.domain.context.service.impl;

import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RelationStatus;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.context.service.PersonContextService;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.entity.PersonTenant;
import com.amachi.app.core.domain.person.service.DomainEntityProvider;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Orquestador de contextos de identidad (SaaS Elite Tier).
 * Garantiza atomicidad entre la relación contractual (PersonTenant) y operativa (Domain Entity).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonContextServiceImpl implements PersonContextService {

    private final PersonTenantRepository personTenantRepository;
    private final List<DomainEntityProvider> domainEntityProviders;


    @Override
    @Transactional
    public void createContext(Person person, Tenant tenant, RoleContext role, DomainContext domain) {
        log.info("[CONTEXT] Creating context for Person: {}, Tenant: {}, Role: {}, Domain: {}",
                person.getId(), tenant.getCode(), role, domain);

        // 1. Idempotencia: Verificar si la relación ya existe
        PersonTenant pt = personTenantRepository.findByPersonAndTenantAndRoleContext(person, tenant, role)
                .orElseGet(() -> {
                    log.debug("[CONTEXT] No existing PersonTenant found. Creating new one.");
                    return personTenantRepository.save(PersonTenant.builder()
                            .person(person)
                            .tenant(tenant)
                            .roleContext(role)
                            .relationStatus(RelationStatus.ACTIVE)
                            .dateRegistered(LocalDateTime.now())
                            .build());
                });

        // 2. Creación de Entidad de Dominio (si aplica)
        if (domain != null) {
            DomainEntityProvider provider = findProviderOrThrow(domain);

            if (!provider.exists(person, tenant, domain)) {
                log.info("[CONTEXT] Creating specific domain entity for context: {}", domain);
                provider.createEntity(person, tenant, domain);
            } else {
                log.debug("[CONTEXT] Domain entity for context {} already exists.", domain);
            }
        }

        // 3. Validación de consistencia final (SaaS Hardened)
        validateConsistency(person, tenant, role, domain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsActiveContext(Person person, Tenant tenant, DomainContext domain) {
        if (domain == null) return false;
        return findProviderOrThrow(domain).exists(person, tenant, domain);
    }

    @Override
    @Transactional
    public void removeContext(Person person, Tenant tenant, RoleContext role, DomainContext domain) {
        log.info("[CONTEXT] Removing context (Soft delete) for Person: {}, Tenant: {}, Role: {}",
                person.getId(), tenant.getCode(), role);

        personTenantRepository.findByPersonAndTenantAndRoleContext(person, tenant, role)
                .ifPresent(pt -> {
                    pt.delete();
                    personTenantRepository.save(pt);
                });

        // TODO: Implement synchronized soft-delete for Domain Entities via Providers
    }

    private DomainEntityProvider findProviderOrThrow(DomainContext domain) {
        return domainEntityProviders.stream()
                .filter(p -> p.supports(domain))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No provider found for DomainContext: " + domain));
    }

    private void validateConsistency(Person person, Tenant tenant, RoleContext role, DomainContext domain) {
        if (!personTenantRepository.existsByPersonAndTenant(person, tenant)) {
            throw new IllegalStateException("[CONTEXT-ERR] PersonTenant creation failed or is inconsistent.");
        }

        if (domain != null) {
            DomainEntityProvider provider = findProviderOrThrow(domain);
            if (!provider.exists(person, tenant, domain)) {
                throw new IllegalStateException("[CONTEXT-ERR] Domain Entity [" + domain + "] was not found after creation.");
            }
        }
        log.info("[CONTEXT] Final consistency check PASSED for Person: {}", person.getId());
    }

}
