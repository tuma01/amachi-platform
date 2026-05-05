package com.amachi.app.core.management.config.bootstrap;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.enums.*;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.utils.AppConstants;
import com.amachi.app.core.common.config.AppBootstrapProperties;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.core.domain.theme.entity.Theme;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import com.amachi.app.core.management.tenant.service.impl.TenantDomainServiceImpl;
import com.amachi.app.core.management.theme.repository.ThemeRepository;
import com.amachi.app.vitalia.medicalcore.hospital.entity.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * BootstrapService (SaaS Elite Hardened).
 * Orquestra la inicialización del sistema usando el nuevo modelo de
 * composición.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BootstrapService {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final AppBootstrapProperties appBootstrapProperties;
    private final ThemeRepository themeRepository;
    private final TenantDomainServiceImpl tenantDomainService;

    // SaaS Elite Provisioning Engine
    private final UserProvisioningService userProvisioningService;

    @Transactional
    public void runBootstrap() {
        try {
            // 0. SYSTEM Context (Bypass for Bootstrap)
            TenantContext.setTenantId(0L);
            TenantContext.setTenantCode("SYSTEM");

            Theme defaultTheme = themeRepository.findByCode(AppConstants.Themes.DEFAULT_THEME_CODE)
                    .orElseThrow(() -> new ResourceNotFoundException(Theme.class.getName(),
                            "error.resource.not.found", AppConstants.Themes.DEFAULT_THEME_CODE));

            // 1. GLOBAL Tenant (System Admin Context)
            Tenant globalTenant = createOrUpdateTenant(appBootstrapProperties.getTenant().getTenantGlobal(),
                    defaultTheme);

            // 2. SuperAdmin (Composition)
            // Se mantiene el contexto SYSTEM para que la resolución de User/Person sea
            // global
            createSuperAdminElite(appBootstrapProperties.getSuperAdmin(), globalTenant);

            // 3. LOCAL Tenant (Standard Hospital)
            Tenant localTenant = createOrUpdateTenant(appBootstrapProperties.getTenant().getTenantLocal(),
                    defaultTheme);

            // 4. TenantAdmin (Composition)
            // Se mantiene el contexto SYSTEM
            createTenantAdminElite(appBootstrapProperties.getTenantAdmin(), localTenant);

            log.info("🚀 Hardened Elite Bootstrap finalizado con éxito");
        } finally {
            TenantContext.clear();
        }
    }

    private void createSuperAdminElite(AppBootstrapProperties.AdminProperties config, Tenant tenant) {
        log.info("🔑 [BOOTSTRAP] Provisionando SuperAdmin: {}", config.getEmail());

        userProvisioningService.provision(ProvisioningRequest.builder()
                .email(config.getEmail())
                .rawPassword(config.getPassword())
                .firstName(config.getFirstName())
                .lastName(config.getLastName())
                .tenant(tenant)
                .roleContext(RoleContext.SUPER_ADMIN)
                .domainContext(DomainContext.SUPER_ADMIN)
                .build());
    }

    private void createTenantAdminElite(AppBootstrapProperties.AdminProperties config, Tenant tenant) {
        log.info("🔑 [BOOTSTRAP] Provisionando TenantAdmin para tenant {}: {}", tenant.getCode(), config.getEmail());

        userProvisioningService.provision(ProvisioningRequest.builder()
                .email(config.getEmail())
                .rawPassword(config.getPassword())
                .firstName(config.getFirstName())
                .lastName(config.getLastName())
                .tenant(tenant)
                .roleContext(RoleContext.ADMIN)
                .domainContext(DomainContext.ADMIN)
                .build());
    }

    private Tenant createOrUpdateTenant(AppBootstrapProperties.BootstrapTenantConfig config, Theme defaultTheme) {
        String code = config.getCode();
        Tenant tenant = tenantRepository.findByCode(code).orElseGet(() -> {
            log.info("🏢 Creando Tenant [{}]..", code);
            Tenant newTenant;
            if (config.getTenantType() == TenantType.HOSPITAL) {
                Hospital hospital = Hospital.builder()
                        .legalName(config.getLegalName())
                        .taxId(config.getTaxId())
                        .medicalLicense(config.getMedicalLicense())
                        .code(code)
                        .name(config.getName())
                        .description(config.getDescription())
                        .active(true)
                        .build();

                if (config.getAddress() != null) {
                    hospital.setContactPhone(config.getAddress().getPhone());
                }
                newTenant = hospital;
            } else {
                newTenant = Tenant.builder()
                        .code(code)
                        .name(config.getName())
                        .description(config.getDescription())
                        .active(true)
                        .build();
            }
            return newTenant;
        });

        if (config.getAddress() != null && tenant.getAddressId() == null) {
            AddressDto addressDto = AddressDto.builder()
                    .streetName(config.getAddress().getStreetName())
                    .city(config.getAddress().getCity())
                    .streetNumber(config.getAddress().getStreetNumber())
                    .stateId(config.getAddress().getStateId())
                    .provinceId(config.getAddress().getProvinceId())
                    .municipalityId(config.getAddress().getMunicipalityId())
                    .countryId(config.getAddress().getCountryId())
                    .build();
            tenantDomainService.handleTenantAddress(tenant, TenantDto.builder().address(addressDto).build());
        }

        if (tenant.getTheme() == null) {
            tenant.setTheme(defaultTheme);
        }

        return tenantRepository.save(tenant);
    }
}