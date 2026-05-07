package com.amachi.app.vitalia.medicalcore.professional.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.ProfessionalRoleContext;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "MED_PROFESSIONAL_INFO",
    indexes = {
        @Index(name = "IDX_PROF_INFO_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_PROF_INFO_PERSON",  columnList = "FK_ID_PERSON"),
        @Index(name = "IDX_PROF_INFO_CURRENT", columnList = "IS_CURRENT")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class ProfessionalInfo extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_PROF_INFO_PERSON"))
    private Person person;

    // Referencia lógica a organización — sin FK física hasta que exista MED_ORGANIZATION
    @Column(name = "ORGANIZATION_ID")
    private Long organizationId;

    @Column(name = "PERIOD_START_DATE", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "PERIOD_END_DATE")
    private LocalDate periodEndDate;

    @Column(name = "IS_CURRENT", nullable = false)
    @Builder.Default
    private Boolean isCurrent = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_CONTEXT", nullable = false, length = 50)
    private ProfessionalRoleContext roleContext;

    @Column(name = "POSITION", length = 100)
    private String position;

    @Column(name = "DEPARTMENT", length = 100)
    private String department;

    @Column(name = "EMPLOYEE_ID_REF", length = 50)
    private String employeeId;

    @Column(name = "LICENSE_NUMBER", length = 100)
    private String licenseNumber;

    @Column(name = "EXPERIENCE_AT_START")
    private Integer yearsOfExperienceAtStart;

    @Column(name = "CONTRACT_TYPE", length = 50)
    private String contractType;

    @Column(name = "WORK_SCHEDULE", length = 100)
    private String workSchedule;

    @Column(name = "SALARY_GRADE", length = 50)
    private String salaryGrade;

    @Column(name = "SUPERVISOR", length = 100)
    private String supervisor;

    @Column(name = "PERFORMANCE_RATING", precision = 3, scale = 1)
    private BigDecimal performanceRating;

    @Column(name = "NOTES", columnDefinition = "TEXT")
    private String notes;

    @PrePersist @PreUpdate
    private void normalize() {
        if (position != null) position = position.trim();
        if (department != null) department = department.trim();
        if (licenseNumber != null) licenseNumber = licenseNumber.trim().toUpperCase();
    }
}
