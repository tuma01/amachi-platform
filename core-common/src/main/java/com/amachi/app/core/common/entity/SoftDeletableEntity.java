package com.amachi.app.core.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.SQLRestriction;

/**
 * Entidad base para recursos que requieren borrado lógico (Soft Delete).
 * Hereda de BaseEntity para incluir ID.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@SQLRestriction("IS_DELETED = false")
public abstract class SoftDeletableEntity extends BaseEntity implements SoftDeletable {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean deleted = false;

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
}
