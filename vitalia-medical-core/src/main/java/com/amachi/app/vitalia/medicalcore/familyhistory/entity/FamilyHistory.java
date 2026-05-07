package com.amachi.app.vitalia.medicalcore.familyhistory.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MED_FAMILY_HISTORY", indexes = {
        @Index(name = "IDX_FAM_HIST_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_FAM_HIST_HISTORY", columnList = "FK_ID_MEDICAL_HISTORY")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class FamilyHistory extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_FAM_HIST_HISTORY"))
    private MedicalHistory medicalHistory;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = true;

    @Column(name = "MOTHER_HEALTH_INFO", length = 300)
    private String motherHealthInfo;

    @Column(name = "FATHER_HEALTH_INFO", length = 300)
    private String fatherHealthInfo;

    @Column(name = "CARDIAC_FAMHISTORY", length = 300)
    private String cardiacFamilyHistory;

    @Column(name = "MENTAL_FAMHISTORY", length = 300)
    private String mentalFamilyHistory;

    @Column(name = "DIABETES_FAMHISTORY", length = 300)
    private String diabetesFamilyHistory;

    @Column(name = "FAMILY_ETHNICITY", length = 100)
    private String familyEthnicity;

    @Column(name = "GENETIC_RISK_INDEX", length = 30)
    private String geneticRiskIndex;

    @Column(name = "ADDITIONAL_NOTES", columnDefinition = "TEXT")
    private String additionalNotes;

    @OneToMany(mappedBy = "familyHistory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HereditaryDisease> hereditaryDiseases = new ArrayList<>();

    @PrePersist @PreUpdate
    private void normalize() {
        if (familyEthnicity != null) familyEthnicity = familyEthnicity.trim().toUpperCase();
        if (geneticRiskIndex != null) geneticRiskIndex = geneticRiskIndex.trim().toUpperCase();
        if (additionalNotes != null) additionalNotes = additionalNotes.trim();
    }
}
