package com.amachi.app.core.management.tenantadmin.service.impl;

import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.annotation.TenantAware;
import com.amachi.app.core.common.enums.DomainContext;
import com.amachi.app.core.common.enums.RoleContext;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.impl.AddressServiceImpl;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import com.amachi.app.core.management.tenantadmin.dto.TenantAdminDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@TenantAware
@RequiredArgsConstructor
@Slf4j
public class TenantAdminDomainServiceImpl {

    private final AddressServiceImpl addressService;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PersonTenantRepository personTenantRepository;
    private final UserProvisioningService userProvisioningService;

    @Transactional
    public void handleTenantAddress(TenantAdmin entity, TenantAdminDto dto) {
        if (dto.getTenant() == null || dto.getTenant().getAddress() == null) {
            return;
        }

        AddressDto addressDto = dto.getTenant().getAddress();
        Tenant tenant = entity.getTenant();

        if (addressDto.getId() == null) {
//            Address address = addressMapper.toEntity(addressDto);
            Address savedAddress = addressService.create(addressDto);
            tenant.setAddressId(savedAddress.getId());
        } else {
            // Security Check (Elite Standard): Confirm the address belongs to this tenant
            if (tenant.getAddressId() != null && !tenant.getAddressId().equals(addressDto.getId())) {
                log.error("Security violation: Tenant {} attempted to access/update unauthorized address ID: {}. Current address ID: {}",
                        tenant.getCode(), addressDto.getId(), tenant.getAddressId());
                throw new IllegalStateException("Unauthorized address modification attempted");
            }

            Address existingAddress = addressService.getById(addressDto.getId());
            addressService.update(addressDto.getId(), addressDto);
            tenant.setAddressId(existingAddress.getId());
        }
    }

    public TenantAdminDto enrichTenantAdminDto(TenantAdmin entity, TenantAdminDto dto) {
        if (entity.getTenant() != null && entity.getTenant().getAddressId() != null) {
            Address address = addressService.getById(entity.getTenant().getAddressId());
            if (dto.getTenant() != null) {
                dto.getTenant().setAddress(addressMapper.toDto(address));
            }
        }
        return dto;
    }

    public void encodePasswordIfNeeded(TenantAdmin entity) {
        if (entity.getUser() != null && entity.getUser().getPassword() != null) {
            entity.getUser().setPassword(passwordEncoder.encode(entity.getUser().getPassword()));
        }
    }

    public void encodePasswordIfPresent(TenantAdmin entity, TenantAdminDto dto) {
        if (dto.getUser() != null && dto.getUser().getPassword() != null && !dto.getUser().getPassword().isBlank()) {
            if (entity.getUser() != null) {
                entity.getUser().setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
            }
        }
    }

    public void resetAdminPassword(TenantAdmin entity, String password) {
        if (entity.getUser() != null) {
            entity.getUser().setPassword(passwordEncoder.encode(password));
            userRepository.save(entity.getUser());
        }
    }

    @Transactional
    public void completeAccountSetup(TenantAdmin savedEntity) {
        log.info("🚀 [DOMAIN] Configurando cuenta via Provisioning para admin: {}",
                savedEntity.getUser() != null ? savedEntity.getUser().getEmail() : null);

        // 1. Password encoding (si no se hizo antes)
        if (savedEntity.getUser() != null && savedEntity.getUser().getPassword() != null 
                && !savedEntity.getUser().getPassword().startsWith("$2a$")) {
            savedEntity.getUser().setPassword(passwordEncoder.encode(savedEntity.getUser().getPassword()));
        }

        // 2. Delegate to Provisioning Service
        userProvisioningService.provision(ProvisioningRequest.builder()
                .email(savedEntity.getUser().getEmail())
                .tenant(savedEntity.getTenant())
                .roleContext(RoleContext.ADMIN)
                .domainContext(DomainContext.ADMIN)
                .resolvedPerson(savedEntity.getPerson())
                .resolvedUser(savedEntity.getUser())
                .build());
    }
}
