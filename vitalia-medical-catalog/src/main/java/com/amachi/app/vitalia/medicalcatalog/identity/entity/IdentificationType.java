package com.amachi.app.vitalia.medicalcatalog.identity.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Maestro de Tipos de Identificación (Catálogo Global compartido).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "CAT_IDENTIFICATION_TYPE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_ID_TYPE_CODE", columnNames = {"CODE"})
    },
    indexes = {
        @Index(name = "IDX_ID_TYPE_CODE", columnList = "CODE")
    }
)
public class IdentificationType extends Auditable<String> implements Model {

    @NotBlank(message = "Code {err.mandatory}")
    @Column(name = "CODE", nullable = false, length = 20)
    private String code; // e.g., CC, PA, TI

    @NotBlank(message = "Name {err.mandatory}")
    @Column(name = "NAME", nullable = false, length = 100)
    private String name; // e.g., Cédula de Ciudadanía, Pasaporte

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
