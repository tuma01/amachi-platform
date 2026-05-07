package com.amachi.app.vitalia.medicalcore.habit.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.SleepQuality;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "MED_PHYSIOLOGICAL_HABIT", indexes = {
        @Index(name = "IDX_HAB_PHYS_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_HAB_PHYS_HISTORY", columnList = "FK_ID_MEDICAL_HISTORY")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class PhysiologicalHabit extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_PHYS_HAB_HISTORY"))
    private MedicalHistory medicalHistory;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = true;

    @Column(name = "NUTRITION", length = 500)
    private String nutrition;

    @Enumerated(EnumType.STRING)
    @Column(name = "SLEEP_QUALITY", length = 30)
    private SleepQuality sleepQuality;

    @Column(name = "SLEEP_HOURS")
    private Integer sleepHours;

    @Column(name = "URINATION", length = 250)
    private String urination;

    @Column(name = "DEFECATION", length = 250)
    private String defecation;

    @Column(name = "SEXUALITY", length = 250)
    private String sexuality;

    @Column(name = "SPORTS_ACTIVITY", length = 500)
    private String sportsActivity;

    @Column(name = "OTHERS", length = 500)
    private String others;

    @PrePersist @PreUpdate
    private void normalize() {
        if (nutrition != null) nutrition = nutrition.trim();
        if (others != null) others = others.trim();
    }
}
