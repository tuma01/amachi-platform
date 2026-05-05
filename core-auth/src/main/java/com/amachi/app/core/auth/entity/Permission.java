package com.amachi.app.core.auth.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.entity.SoftDeletable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.experimental.SuperBuilder;
import lombok.*;

@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "AUT_PERMISSION", uniqueConstraints = {
        @UniqueConstraint(name = "UK_NAME_PERMISSION", columnNames = { "NAME" })
})
public class Permission extends Auditable<String> implements SoftDeletable, Model {

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

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @NotBlank(message = "Permission name cannot be empty")
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @PrePersist
    @PreUpdate
    private void normalizePermission() {
        if (this.name != null) {
            this.name = this.name.toUpperCase().trim();
        }
    }
}
