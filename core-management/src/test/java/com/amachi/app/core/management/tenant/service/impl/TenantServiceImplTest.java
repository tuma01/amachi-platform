package com.amachi.app.core.management.tenant.service.impl;

import com.amachi.app.core.auth.context.AuthContextHolder;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.exception.UnauthorizedException;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.event.DomainEventPublisher;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.domain.tenant.repository.TenantRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceImplTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private TenantServiceImpl service;

    // ===============================
    // ENABLE TENANT
    // ===============================

    @Test
    void enableTenant_WhenExists_ThenSetActiveTrue() {
        Long id = 1L;

        Tenant entity = Instancio.of(Tenant.class)
                .set(field(Tenant::getId), id)
                .set(field(Tenant::getActive), false)
                .create();

        when(tenantRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            service.enableTenant(id);

            assertTrue(entity.getActive());
            verify(tenantRepository).save(entity);
        }
    }

    @Test
    void enableTenant_WhenNotExists_ThenThrowResourceNotFoundException() {
        Long id = 999L;

        when(tenantRepository.findById(id))
                .thenReturn(Optional.empty());

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            assertThrows(ResourceNotFoundException.class, () -> service.enableTenant(id));
        }
    }

    @Test
    void enableTenant_WhenNotSuperAdmin_ThenThrowUnauthorizedException() {
        Long id = 1L;

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(false);

            assertThrows(UnauthorizedException.class, () -> service.enableTenant(id));

            verify(tenantRepository, never()).findById(any());
            verify(tenantRepository, never()).save(any());
        }
    }

    // ===============================
    // DISABLE TENANT
    // ===============================

    @Test
    void disableTenant_WhenExists_ThenSetActiveFalse() {
        Long id = 1L;

        Tenant entity = Instancio.of(Tenant.class)
                .set(field(Tenant::getId), id)
                .set(field(Tenant::getActive), true)
                .create();

        when(tenantRepository.findById(id))
                .thenReturn(Optional.of(entity));

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            service.disableTenant(id);

            assertFalse(entity.getActive());
            verify(tenantRepository).save(entity);
        }
    }

    @Test
    void disableTenant_WhenNotExists_ThenThrowResourceNotFoundException() {
        Long id = 999L;

        when(tenantRepository.findById(id))
                .thenReturn(Optional.empty());

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            assertThrows(ResourceNotFoundException.class, () -> service.disableTenant(id));
        }
    }

    @Test
    void disableTenant_WhenNotSuperAdmin_ThenThrowUnauthorizedException() {
        Long id = 1L;

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(false);

            assertThrows(UnauthorizedException.class, () -> service.disableTenant(id));

            verify(tenantRepository, never()).findById(any());
            verify(tenantRepository, never()).save(any());
        }
    }

    // ===============================
    // CREATE
    // ===============================

    @Test
    void create_WhenValidEntity_ThenReturnSavedEntity() {
        Tenant entity = Instancio.create(Tenant.class);

        when(tenantRepository.save(any(Tenant.class))).thenReturn(entity);

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            // 🔴 NECESARIO
            TenantContext.setTenantId(1L);

            try {
                Tenant result = service.create(entity);

                assertNotNull(result);
                verify(tenantRepository).save(entity);
            } finally {
                TenantContext.clear(); // 🔴 limpiar siempre
            }
        }
    }

    @Test
    void create_WhenNotSuperAdmin_ThenThrowUnauthorizedException() {
        Tenant entity = Instancio.create(Tenant.class);

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(false);

            assertThrows(UnauthorizedException.class, () -> service.create(entity));

            verify(tenantRepository, never()).save(any());
        }
    }

    // ===============================
    // UPDATE
    // ===============================

    @Test
    void update_WhenExists_ThenReturnUpdatedEntity() {
        Long id = 1L;

        Tenant entity = Instancio.of(Tenant.class)
                .set(field(Tenant::getId), id)
                .create();

        when(tenantRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(tenantRepository.save(any(Tenant.class)))
                .thenReturn(entity);

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            Tenant result = service.update(id, entity);

            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(tenantRepository).save(entity);
        }
    }

    @Test
    void update_WhenNotExists_ThenThrowResourceNotFoundException() {
        Long id = 999L;
        Tenant entity = Instancio.create(Tenant.class);

        when(tenantRepository.findById(id))
                .thenReturn(Optional.empty());

        try (MockedStatic<AuthContextHolder> mocked = Mockito.mockStatic(AuthContextHolder.class)) {
            mocked.when(AuthContextHolder::isSuperAdmin).thenReturn(true);

            assertThrows(ResourceNotFoundException.class, () -> service.update(id, entity));
        }
    }

    // ===============================
    // GET BY ID
    // ===============================

    @Test
    void getById_WhenExists_ThenReturnEntity() {
        Long id = 1L;

        Tenant entity = Instancio.of(Tenant.class)
                .set(field(Tenant::getId), id)
                .create();

        when(tenantRepository.findById(id))
                .thenReturn(Optional.of(entity));

        Tenant result = service.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getById_WhenNotExists_ThenThrowResourceNotFoundException() {
        Long id = 999L;

        when(tenantRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getById(id));
    }
}
