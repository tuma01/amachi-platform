package com.amachi.app.core.domain.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.entity.SoftDeletable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * Representa una organización externa o interna (Hospital, Clínica, Aseguradora).
 */
@Entity
@Table(name = "DMN_ORGANIZATION")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Audited
@Schema(description = "Entidad genérica para organizaciones corporativas")
public class Organization extends Auditable<String> implements Model, SoftDeletable {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    @Override
    public void delete() {
        this.isDeleted = true;
    }

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    @Column(name = "LEGAL_IDENTIFIER", length = 50)
    private String legalIdentifier;

    @Column(name = "TYPE", length = 50)
    private String type; // e.g., Hospital, Clinic, Insurance

    @Column(name = "ADDRESS", length = 250)
    private String address;

    @Column(name = "CONTACT_PHONE", length = 50)
    private String contactPhone;

    @Column(name = "ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
