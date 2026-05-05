package com.amachi.app.core.management.superadmin.entity;

import com.amachi.app.core.auth.entity.User;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.common.enums.SuperAdminLevel;
import com.amachi.app.core.domain.entity.Person;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.*;


/**
 * Entidad de Gestión de Plataforma (SaaS Elite Tier).
 * Usa composición para vincularse a la identidad global (Person).
 */
@Entity
@Table(name = "MGT_SUPER_ADMIN")
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SuperAdmin extends AuditableTenantEntity implements Model {


    @Enumerated(EnumType.STRING)
    @Column(name = "LEVEL", nullable = false, length = 30)
    private SuperAdminLevel level;

    @Column(name = "GLOBAL_ACCESS", nullable = false)
    private Boolean globalAccess;

    /**
     * Vínculo a la identidad global (Composición pura).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false, foreignKey = @ForeignKey(name = "FK_SUPER_ADMIN_PERSON"))
    private Person person;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_SUPER_ADMIN_USER"))
    private User user;

    @PrePersist
    private void validate() {
        if (this.user == null) {
            throw new IllegalStateException("SuperAdmin.user cannot be null at persist time");
        }
    }
}
