package com.amachi.app.core.management.tenantadmin.service.impl;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.repository.UserRepository;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.repository.PersonTenantRepository;
import com.amachi.app.core.domain.tenant.dto.TenantDto;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.geography.address.dto.AddressDto;
import com.amachi.app.core.geography.address.entity.Address;
import com.amachi.app.core.geography.address.mapper.AddressMapper;
import com.amachi.app.core.geography.address.service.impl.AddressServiceImpl;
import com.amachi.app.core.management.provisioning.dto.ProvisioningRequest;
import com.amachi.app.core.management.provisioning.service.UserProvisioningService;
import com.amachi.app.core.management.tenantadmin.dto.TenantAdminDto;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantAdminDomainServiceImplTest extends AbstractTestSupport {

    @Mock
    private AddressServiceImpl addressService;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PersonTenantRepository personTenantRepository;
    @Mock
    private UserProvisioningService userProvisioningService;

    @InjectMocks
    private TenantAdminDomainServiceImpl domainService;

    // --- Address Logic Tests ---

    @Test
    void handleTenantAddress_WhenCreate_ThenCreateAddress() {
        // Arrange
        TenantAdmin entity = Instancio.create(TenantAdmin.class);
        entity.getTenant().setAddressId(null); 

        AddressDto addressDto = Instancio.of(AddressDto.class)
                .set(field(AddressDto::getId), null)
                .create();

        TenantAdminDto dto = Instancio.of(TenantAdminDto.class)
                .set(field(TenantAdminDto::getTenant), Instancio.of(com.amachi.app.core.domain.tenant.dto.TenantDto.class)
                        .set(field(com.amachi.app.core.domain.tenant.dto.TenantDto::getAddress), addressDto)
                        .create())
                .create();

        Address createdAddress = Instancio.create(Address.class);
        when(addressService.create(any())).thenReturn(createdAddress);

        // Act
        domainService.handleTenantAddress(entity, dto);

        // Assert
        assertEquals(createdAddress.getId(), entity.getTenant().getAddressId());
    }

    @Test
    void handleTenantAddress_WhenSecurityViolation_ThenThrowException() {
        // Arrange
        Long originalId = 100L;
        Long hackingId = 200L;

        TenantAdmin entity = Instancio.create(TenantAdmin.class);
        entity.getTenant().setAddressId(originalId);

        AddressDto addressDto = Instancio.of(AddressDto.class)
                .set(field(AddressDto::getId), hackingId)
                .create();

        TenantAdminDto dto = Instancio.of(TenantAdminDto.class)
                .set(field(TenantAdminDto::getTenant), Instancio.of(TenantDto.class)
                        .set(field(TenantDto::getAddress), addressDto)
                        .create())
                .create();

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> domainService.handleTenantAddress(entity, dto));
    }

    // --- Account Setup Logic Tests ---

    @Test
    void completeAccountSetup_WhenValid_ThenDelegatesToProvisioning() {
        // Arrange
        TenantAdmin savedEntity = Instancio.create(TenantAdmin.class);
        savedEntity.setUser(Instancio.create(User.class));
        savedEntity.setTenant(Instancio.create(Tenant.class));
        savedEntity.setPerson(Instancio.create(Person.class));

        // Act
        domainService.completeAccountSetup(savedEntity);

        // Assert
        verify(userProvisioningService).provision(argThat(request -> 
                request.getEmail().equals(savedEntity.getUser().getEmail()) &&
                request.getTenant().equals(savedEntity.getTenant()) &&
                request.getResolvedPerson().equals(savedEntity.getPerson()) &&
                request.getResolvedUser().equals(savedEntity.getUser())
        ));
    }
}
