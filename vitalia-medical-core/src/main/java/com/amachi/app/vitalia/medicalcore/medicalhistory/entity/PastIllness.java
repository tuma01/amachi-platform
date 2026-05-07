package com.amachi.app.vitalia.medicalcore.medicalhistory.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "MED_PAST_ILLNESS", indexes = {
        @Index(name = "IDX_PAST_ILL_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_PAST_ILL_HISTORY", columnList = "FK_ID_HIST_MED")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class PastIllness extends AuditableTenantEntity implements Model {

    @Column(name = "DISEASE_NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "SYMPTOMS", length = 500)
    private String symptoms;

    @Column(name = "TREATMENTS", length = 500)
    private String treatments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_HIST_MED", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_PAST_ILL_HISTORY"))
    private MedicalHistory medicalHistory;

    @PrePersist @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim().toUpperCase();
    }
}
