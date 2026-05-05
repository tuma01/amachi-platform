package com.amachi.app.core.auth.service.impl;

import com.amachi.app.core.auth.dto.UserTenantRoleDto;
import com.amachi.app.core.auth.mapper.UserTenantRoleMapper;
import com.amachi.app.core.auth.dto.search.UserTenantRoleSearchDto;
import com.amachi.app.core.auth.entity.Role;
import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.auth.entity.UserTenantRole;
import com.amachi.app.core.auth.repository.RoleRepository;
import com.amachi.app.core.auth.repository.UserTenantRoleRepository;
import com.amachi.app.core.common.context.TenantContext;
import com.amachi.app.core.common.test.util.AbstractTestSupport;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.mockito.Spy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTenantRoleServiceImplTest extends AbstractTestSupport {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserTenantRoleRepository userTenantRoleRepository;

    @Mock
    private UserTenantRoleMapper mapper;

    @Mock
    private EntityManager em;

    @InjectMocks
    @Spy
    private UserTenantRoleServiceImpl service;

    private static final String DATA_PATH = "data/usertenantrole/";
    private UserTenantRole entity;
    private UserTenantRoleSearchDto searchDto;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId(1L); // ✅ obligatorio
        entity = loadJson(DATA_PATH + "utr-entity.json", UserTenantRole.class);
        searchDto = loadJson(DATA_PATH + "utr-search-dto.json", UserTenantRoleSearchDto.class);
    }

    @AfterEach
    void cleanup() {
        TenantContext.clear();
    }

    @Test
    void getAll_ShouldReturnList() {
        when(userTenantRoleRepository.findAll(isA(Specification.class)))
                .thenReturn(List.of(entity));

        List<UserTenantRole> result = service.getAll();

        assertThat(result).hasSize(1);
        verify(userTenantRoleRepository).findAll(any(Specification.class));
    }

    @Test
    void getAllWithPagination_ShouldReturnPage() {
        Page<UserTenantRole> page = new PageImpl<>(List.of(entity));
        when(userTenantRoleRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);

        Page<UserTenantRole> result = service.getAll(searchDto, 0, 10);

        assertThat(result.getContent()).hasSize(1);
        verify(userTenantRoleRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        entity.setDeleted(false);
        entity.setTenantId(1L);

        when(userTenantRoleRepository.findByIdAndTenantId(1L, 1L))
                .thenReturn(Optional.of(entity));

        UserTenantRole result = service.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {

        when(userTenantRoleRepository.findByIdAndTenantId(1L, 1L))
                .thenReturn(Optional.empty());

        TenantContext.setTenantId(1L); // 🔥 obligatorio

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_ShouldSaveAndReturn() {
        UserTenantRoleDto dto = mock(UserTenantRoleDto.class);
        when(userTenantRoleRepository.save(any(UserTenantRole.class))).thenReturn(entity);
        doReturn(entity).when(service).mapToEntity(dto);

        UserTenantRole result = service.create(dto);

        assertThat(result).isNotNull();
        verify(userTenantRoleRepository).save(any(UserTenantRole.class));
    }

    @Test
    void update_WhenExists_ShouldUpdateAndReturn() {
        UserTenantRoleDto dto = mock(UserTenantRoleDto.class);
        TenantContext.setTenantId(1L); // 🔥 obligatorio

        when(userTenantRoleRepository.findByIdAndTenantId(1L, 1L))
                .thenReturn(Optional.of(entity));
        when(userTenantRoleRepository.save(any(UserTenantRole.class)))
                .thenReturn(entity);

        doNothing().when(service).mergeEntities(eq(dto), any(UserTenantRole.class));
        UserTenantRole result = service.update(1L, dto);
        assertThat(result).isNotNull();
        verify(userTenantRoleRepository).save(any(UserTenantRole.class));
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(userTenantRoleRepository.findByIdAndTenantId(1L, 1L)).thenReturn(Optional.of(entity));

        service.delete(1L);

        verify(userTenantRoleRepository).save(entity);
        assertThat(entity.getDeleted()).isTrue();
//        verify(userTenantRoleRepository).delete(entity);
    }

    @Test
    void assignRolesToUserAndTenant_ByIds_ShouldSaveAll() {
        List<String> roles = List.of("ROLE_ADMIN");
        when(roleRepository.findByNameIn(roles)).thenReturn(List.of(entity.getRole()));
        when(em.getReference(eq(User.class), anyLong())).thenReturn(entity.getUser());

        service.assignRolesToUserAndTenant(1L, 1L, roles);

        verify(userTenantRoleRepository).saveAll(anyList());
    }

    @Test
    void assignRolesToUserAndTenant_ByEntities_ShouldReturnSavedSet() {

        Set<Role> roles = Set.of(entity.getRole());

        when(em.getReference(eq(User.class), anyLong()))
                .thenReturn(entity.getUser());
        when(userTenantRoleRepository
                .existsByUserAndTenantIdAndRoleAndDeletedFalse(any(), anyLong(), any()))
                .thenReturn(false);
        when(userTenantRoleRepository.saveAll(anyCollection()))
                .thenReturn(List.of(entity));

        Set<UserTenantRole> result =
                service.assignRolesToUserAndTenant(entity.getUser(), entity.getTenantId(), roles);
        assertThat(result).hasSize(1);
        verify(userTenantRoleRepository).saveAll(anyCollection());
    }

    @Test
    void findRoleNamesByUserAndTenant_ShouldReturnSet() {
        when(userTenantRoleRepository.findActiveRoleNamesByUserAndTenant(1L, 1L)).thenReturn(List.of("ROLE_ADMIN"));

        Set<String> result = service.findRoleNamesByUserAndTenant(1L, 1L);

        assertThat(result).contains("ROLE_ADMIN");
    }

    @Test
    void revokeRole_ShouldDeactivateAndSetRevokedAt() {
        when(em.getReference(eq(User.class), anyLong())).thenReturn(entity.getUser());
        when(userTenantRoleRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        service.revokeRole(1L, 1L, "ROLE_ADMIN");

        assertThat(entity.isActive()).isFalse();
        assertThat(entity.getRevokedAt()).isNotNull();
        verify(userTenantRoleRepository).save(entity);
    }
}
