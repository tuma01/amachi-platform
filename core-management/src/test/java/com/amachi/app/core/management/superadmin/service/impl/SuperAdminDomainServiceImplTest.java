package com.amachi.app.core.management.superadmin.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.config.AppBootstrapProperties;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import com.amachi.app.core.management.superadmin.entity.SuperAdmin;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuperAdminDomainServiceImplTest extends AbstractTestSupport {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TenantRepository tenantRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AppBootstrapProperties appBootstrapProperties;
    @Mock
    private UserProvisioningService userProvisioningService;

    // Mocks for deep property access
    @Mock
    private AppBootstrapProperties.TenantProperties tenantProperties;
    @Mock
    private AppBootstrapProperties.TenantGlobal tenantGlobal;

    @InjectMocks
    private SuperAdminDomainServiceImpl domainService;

    @Test
    void completeAccountSetup_WhenGlobalTenantExists_ThenDelegatesToProvisioning() {
        // Arrange
        SuperAdmin savedEntity = Instancio.create(SuperAdmin.class);
        savedEntity.setPerson(Instancio.create(Person.class));
        savedEntity.setUser(Instancio.create(User.class));

        String globalCode = "GLOBAL";
        Tenant globalTenant = Instancio.of(Tenant.class)
                .set(field(Tenant::getCode), globalCode)
                .create();

        when(appBootstrapProperties.getTenant()).thenReturn(tenantProperties);
        when(tenantProperties.getTenantGlobal()).thenReturn(tenantGlobal);
        when(tenantGlobal.getCode()).thenReturn(globalCode);
        when(tenantRepository.findByCode(globalCode)).thenReturn(Optional.of(globalTenant));

        // Act
        domainService.completeAccountSetup(savedEntity);

        // Assert
        verify(userProvisioningService).provision(argThat(request -> 
                request.getEmail().equals(savedEntity.getUser().getEmail()) &&
                request.getTenant().equals(globalTenant) &&
                request.getResolvedPerson().equals(savedEntity.getPerson()) &&
                request.getResolvedUser().equals(savedEntity.getUser())
        ));
    }

    @Test
    void completeAccountSetup_WhenGlobalTenantMissing_ThenThrowException() {
        // Arrange
        SuperAdmin savedEntity = Instancio.create(SuperAdmin.class);
        savedEntity.setPerson(Instancio.create(Person.class));
        savedEntity.setUser(Instancio.create(User.class));
        
        String globalCode = "GLOBAL";

        when(appBootstrapProperties.getTenant()).thenReturn(tenantProperties);
        when(tenantProperties.getTenantGlobal()).thenReturn(tenantGlobal);
        when(tenantGlobal.getCode()).thenReturn(globalCode);
        when(tenantRepository.findByCode(globalCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> domainService.completeAccountSetup(savedEntity));
        verify(userProvisioningService, never()).provision(any());
    }

    @Test
    void encodePasswordIfNeeded_WhenNewUser_ThenEncode() {
        // Arrange
        SuperAdmin entity = Instancio.create(SuperAdmin.class);
        User user = entity.getUser();
        user.setId(null); 
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        // Act
        domainService.encodePasswordIfNeeded(entity);

        // Assert
        assertEquals("encodedPassword", user.getPassword());
    }
}
