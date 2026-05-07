package com.amachi.app.vitalia.medicalcore.insurance.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcatalog.healthcareprovider.entity.HealthcareProvider;
import com.amachi.app.vitalia.medicalcore.medicalhistory.entity.MedicalHistory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "MED_INSURANCE", indexes = {
        @Index(name = "IDX_INS_TENANT",   columnList = "TENANT_ID"),
        @Index(name = "IDX_INS_HISTORY",  columnList = "FK_ID_MEDICAL_HISTORY"),
        @Index(name = "IDX_INS_PROVIDER", columnList = "FK_ID_HEALTHCARE_PROVIDER")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Insurance extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_MEDICAL_HISTORY", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_INS_HISTORY"))
    private MedicalHistory medicalHistory;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_HEALTHCARE_PROVIDER", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_INS_PROVIDER"))
    private HealthcareProvider provider;

    @Column(name = "POLICY_NUMBER", length = 50)
    private String policyNumber;

    @Column(name = "POLICY_TYPE", length = 50)
    private String policyType;

    @Column(name = "EFFECTIVE_DATE")
    private LocalDate effectiveDate;

    @Column(name = "EXPIRATION_DATE")
    private LocalDate expirationDate;

    @Column(name = "COVERAGE_DETAILS", length = 1000)
    private String coverageDetails;

    @Column(name = "COPAY_AMOUNT", precision = 12, scale = 2)
    private BigDecimal copayAmount;

    @Column(name = "DEDUCTIBLE_AMOUNT", precision = 12, scale = 2)
    private BigDecimal deductibleAmount;

    @Column(name = "AUTH_REQUIRED")
    @Builder.Default
    private Boolean requiresAuthorization = false;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = true;
}
