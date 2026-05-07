package com.amachi.app.vitalia.medicalcore.employee.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.EmployeeStatus;
import com.amachi.app.core.common.enums.EmployeeType;
import com.amachi.app.core.common.enums.ShiftEnum;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "MED_EMPLOYEE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_EMP_TENANT_CODE",
            columnNames = {"TENANT_ID", "EMPLOYEE_CODE", "IS_DELETED"}),
        @UniqueConstraint(name = "UK_EMP_IDENTITY_TENANT",
            columnNames = {"FK_ID_PERSON", "TENANT_ID", "IS_DELETED"})
    },
    indexes = {
        @Index(name = "IDX_EMP_TENANT",    columnList = "TENANT_ID"),
        @Index(name = "IDX_EMP_PERSON",    columnList = "FK_ID_PERSON"),
        @Index(name = "IDX_EMP_CODE",      columnList = "TENANT_ID, EMPLOYEE_CODE"),
        @Index(name = "IDX_EMP_DEPT_UNIT", columnList = "FK_ID_DEPT_UNIT"),
        @Index(name = "IDX_EMP_STATUS",    columnList = "EMPLOYEE_STATUS")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Employee extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_EMP_PERSON"))
    private Person person;

    // Referencia lógica al usuario del sistema — sin @ManyToOne para no acoplar con core-auth
    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DEPT_UNIT",
                foreignKey = @ForeignKey(name = "FK_MED_EMP_DEPT_UNIT"))
    private DepartmentUnit departmentUnit;

    @Column(name = "EMPLOYEE_CODE", length = 50)
    private String employeeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_TYPE", nullable = false, length = 40)
    private EmployeeType employeeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "EMPLOYEE_STATUS", nullable = false, length = 30)
    @Builder.Default
    private EmployeeStatus employeeStatus = EmployeeStatus.ACTIVO;

    @Column(name = "PROFESSIONAL_ROLE", length = 50)
    private String professionalRole;

    @Column(name = "JOB_POSITION", length = 120)
    private String jobPosition;

    @Column(name = "HIRE_DATE")
    private LocalDate hireDate;

    @Column(name = "SALARY", precision = 12, scale = 2)
    private BigDecimal salary;

    @Column(name = "EMPLOYMENT_TYPE", length = 50)
    private String employmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "WORK_SHIFT", length = 50)
    private ShiftEnum workShift;

    @Column(name = "EMERGENCY_CONTACT", length = 200)
    private String emergencyContact;

    @PrePersist @PreUpdate
    private void normalize() {
        if (employeeCode != null) employeeCode = employeeCode.trim().toUpperCase();
        if (jobPosition != null) jobPosition = jobPosition.trim();
    }
}
