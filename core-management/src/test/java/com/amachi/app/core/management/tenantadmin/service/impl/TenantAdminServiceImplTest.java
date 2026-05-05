package com.amachi.app.core.management.tenantadmin.service.impl; // Convention: test in same package as impl or generic service package (User requested service)

import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import com.amachi.app.core.management.tenantadmin.entity.TenantAdmin;
import com.amachi.app.core.management.tenantadmin.repository.TenantAdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantAdminServiceImplTest extends AbstractTestSupport {

    @Mock
    private TenantAdminRepository tenantAdminRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private TenantAdminDomainServiceImpl tenantAdminDomainService;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private TenantAdminServiceImpl service;

    private static final String DATA_PATH = "data/tenantadmin/";

    @Test
    void createTenantAdmin_WhenValidEntity_ThenSaveAndReturn() {

        // 🔥 SET CONTEXT
        TenantContext.setTenantId(1L);

        try {
            // Arrange
            TenantAdmin entity = loadJson(DATA_PATH + "tenantadmin-entity.json", TenantAdmin.class);

            when(tenantRepository.findById(1L)).thenReturn(Optional.of(entity.getTenant()));
            when(tenantAdminRepository.save(any(TenantAdmin.class))).thenReturn(entity);

            // Act
            TenantAdmin result = service.create(entity);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getPerson().getFirstName()).isEqualTo("Felix");
            assertThat(result.getTenant().getId()).isEqualTo(1L);

            verify(tenantRepository).findById(1L);
            verify(tenantAdminRepository).save(entity);
            verify(tenantAdminDomainService).completeAccountSetup(entity);

        } finally {
            // 🔥 CLEANUP (MUY IMPORTANTE)
            TenantContext.clear();
        }
    }
}
