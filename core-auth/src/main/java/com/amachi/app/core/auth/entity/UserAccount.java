package com.amachi.app.core.auth.entity;

import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Objects;

/**
 * Entidad UserAccount (SaaS Elite Tier).
 * Vincula una identidad global (User) con un contexto específico (Tenant).
 * (Manual Implementation to ensure definitive build stability)
 */
@Entity
@Table(
    name = "AUT_USER_ACCOUNT",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_USER_TENANT_ACCOUNT", columnNames = {"FK_ID_USER", "TENANT_ID"})
    },
    indexes = {
        @Index(name = "IDX_USER_ACCOUNT_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_USER_ACCOUNT_PERSON", columnList = "FK_ID_PERSON")
    }
)
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Link between global user identity and specific tenant context")
public class UserAccount extends AuditableTenantEntity implements Model {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_ACCOUNT_USER"))
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_ACCOUNT_PERSON"))
    private Person person;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "FK_ID_TENANT",
        referencedColumnName = "ID",
        nullable = false,
        foreignKey = @ForeignKey(name = "FK_USER_ACCOUNT_TENANT")
    )
    private Tenant tenant; // Reference to the actual Tenant entity for business logic

    @PrePersist
    @PreUpdate
    private void normalizeAccount() {
        if (this.tenant != null && getTenantCode() == null) {
            setTenantCode(this.tenant.getCode());
        }
    }

    public User getUser() {
        return user;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public Long getPersonId() {
        return person != null ? person.getId() : null;
    }

    public Long getTenantPk() {
        return tenant != null ? tenant.getId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount account)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(user, account.user) && Objects.equals(person, account.person) && Objects.equals(tenant, account.tenant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, person, tenant);
    }
}
