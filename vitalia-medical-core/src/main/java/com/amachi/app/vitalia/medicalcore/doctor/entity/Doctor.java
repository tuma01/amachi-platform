package com.amachi.app.vitalia.medicalcore.doctor.entity;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.envers.Audited;

@Entity
@Table(
    name = "MED_DOCTOR",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_DOC_TENANT_LICENSE", columnNames = {"TENANT_ID", "LICENSE_NUMBER", "IS_DELETED"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Audited
public class Doctor extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false, foreignKey = @ForeignKey(name = "FK_DOCTOR_PERSON"))
    private Person person;

    @Column(name = "LICENSE_NUMBER", nullable = false, length = 50)
    private String licenseNumber;

    @Column(name = "PROVIDER_NUMBER", length = 50)
    private String providerNumber;

    @Column(name = "SPECIALTIES_SUMMARY", length = 255)
    private String specialtiesSummary;

    @Column(name = "BIO", columnDefinition = "TEXT")
    private String bio;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_DEPT_UNIT", foreignKey = @ForeignKey(name = "FK_MED_DR_DEPT"))
//    private DepartmentUnit departmentUnit;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "FK_ID_EMPLOYEE", foreignKey = @ForeignKey(name = "FK_MED_DR_EMP"))
//    private Employee employee;

    @Column(name = "CONSULTATION_PRICE", precision = 12, scale = 2)
    private BigDecimal consultationPrice;

    @Column(name = "SIGNATURE_PATH", length = 255)
    private String signatureDigitalPath;

    @Column(name = "LICENSE_EXPIRY_DATE")
    private LocalDate licenseExpiryDate;

    @Column(name = "HIRE_DATE")
    private LocalDate hireDate;

    @Column(name = "OFFICE_NUMBER", length = 20)
    private String officeNumber;

    @Column(name = "YEARS_EXPERIENCE")
    private Integer yearsOfExperience;

    @Column(name = "TOTAL_CONSULTATIONS")
    @Builder.Default
    private Integer totalConsultations = 0;

    @Column(name = "RATING")
    private Double rating;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private boolean isActive = true;
}
