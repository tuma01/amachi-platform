package com.amachi.app.core.auth.entity;

import com.amachi.app.core.common.entity.BaseEntity;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "AUT_ROLE_PERMISSION", uniqueConstraints = {
        @UniqueConstraint(name = "UK_ROLE_PERMISSION", columnNames = { "ROLE_ID", "PERMISSION_ID" })
})
public class RolePermission extends BaseEntity implements Model {

    // ID heredado de BaseEntity

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_ROLE", nullable = false, foreignKey = @ForeignKey(name = "FK_ROLE_PERMISSION_ROLE"))
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERMISSION", nullable = false, foreignKey = @ForeignKey(name = "FK_ROLE_PERMISSION_PERMISSION"))
    private Permission permission;
}
