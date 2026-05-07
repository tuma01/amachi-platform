package com.amachi.app.vitalia.medicalcore.habit.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.AlcoholConsumption;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(name = "MED_TOXIC_HABIT", indexes = {
        @Index(name = "IDX_TOX_HAB_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_TOX_HAB_HISTORY", columnList = "FK_ID_MEDICAL_HISTORY")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class ToxicHabit extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_TOX_HAB_HISTORY"))
    private MedicalHistory medicalHistory;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "ALCOHOL", nullable = false, length = 50)
    private AlcoholConsumption alcohol;

    @Column(name = "TOBACCO", length = 200)
    private String tobacco;

    @Column(name = "CIGARETTES_PER_DAY")
    private Integer cigarettesPerDay;

    @Column(name = "START_AGE")
    private Integer startAge;

    @Column(name = "QUIT_DATE")
    private LocalDate quitDate;

    @Column(name = "DRUGS", length = 500)
    private String drugs;

    @Column(name = "CAFFEINE", length = 200)
    private String caffeine;

    @Column(name = "SEDENTARY_LIFESTYLE", length = 200)
    private String sedentaryLifestyle;

    @Column(name = "OTHERS", length = 500)
    private String others;

    @PrePersist @PreUpdate
    private void normalize() {
        if (tobacco != null) tobacco = tobacco.trim();
        if (drugs != null) drugs = drugs.trim();
        if (others != null) others = others.trim();
    }
}
