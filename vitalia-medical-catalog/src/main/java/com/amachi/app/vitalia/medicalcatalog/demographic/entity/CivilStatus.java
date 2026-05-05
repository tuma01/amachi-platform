package com.amachi.app.vitalia.medicalcatalog.demographic.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_CIVIL_STATUS",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_CIVIL_STATUS_CODE", columnNames = {"CODE"})
    }
)
public class CivilStatus extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code; // e.g., SINGLE, MARRIED

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 50)
    private String name; // e.g., Soltero/a, Casado/a

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.code != null) this.code = this.code.trim().toUpperCase();
        if (this.name != null) this.name = this.name.trim();
    }
}
