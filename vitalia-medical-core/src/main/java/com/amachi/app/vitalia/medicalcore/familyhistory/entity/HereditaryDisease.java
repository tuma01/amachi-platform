package com.amachi.app.vitalia.medicalcore.familyhistory.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.kinship.entity.Kinship;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDate;

@Entity
@Table(name = "MED_HEREDITARY_DISEASE", indexes = {
        @Index(name = "IDX_HER_DIS_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_HER_DIS_FAM",    columnList = "FK_ID_HIST_FAM")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class HereditaryDisease extends AuditableTenantEntity implements Model {

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_KINSHIP",
                foreignKey = @ForeignKey(name = "FK_MED_HER_DIS_KINSHIP"))
    private Kinship kinship;

    @Column(name = "REMARK", length = 500)
    private String remark;

    @Column(name = "DIAGNOSIS_DATE")
    private LocalDate diagnosisDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_HIST_FAM", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_HER_DIS_FAM_HIST"))
    private FamilyHistory familyHistory;

    @PrePersist @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim();
        if (remark != null) remark = remark.trim();
    }
}
