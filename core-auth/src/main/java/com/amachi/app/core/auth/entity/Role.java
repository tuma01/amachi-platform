package com.amachi.app.core.auth.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.SuperBuilder;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad que representa un Rol de Seguridad (SaaS Elite Tier).
 */
@Entity
@Table(name = "AUT_ROLE", uniqueConstraints = {
    @UniqueConstraint(name = "UK_ROLE_NAME", columnNames = {"NAME"})
})
@Getter
@SuperBuilder @Setter
@NoArgsConstructor @AllArgsConstructor

@Schema(description = "Security roles define system-wide or tenant-specific permissions")
public class Role extends Auditable<String> implements Model {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    /**
     * SCHEMA COMPATIBILITY FIELDS
     * Logically global, physically scoped in legacy DB.
     */
    @Column(name = "TENANT_ID", nullable = false)
    @Builder.Default
    private Long tenantId = 0L;

    @Column(name = "TENANT_CODE", nullable = false, length = 50)
    @Builder.Default
    private String tenantCode = "GLOBAL";

    @NotBlank(message = "Role name cannot be empty")
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "AUT_ROLE_PERMISSION",
        joinColumns = @JoinColumn(name = "FK_ID_ROLE", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "FK_ID_PERMISSION", referencedColumnName = "ID")
    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    public void addPermission(Permission permission) {
        if (this.permissions == null) {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        if (this.permissions != null) {
            this.permissions.remove(permission);
        }
    }

    @PrePersist
    @PreUpdate
    private void normalizeRole() {
        if (this.name != null) {
            this.name = this.name.toUpperCase().trim();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
