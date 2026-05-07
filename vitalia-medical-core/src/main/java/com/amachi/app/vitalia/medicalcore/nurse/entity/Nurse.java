package com.amachi.app.vitalia.medicalcore.nurse.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.ShiftEnum;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.core.domain.entity.Person;
import com.amachi.app.vitalia.medicalcore.infrastructure.entity.DepartmentUnit;
import com.amachi.app.vitalia.medicalcore.profile.entity.UserProfile;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "MED_NURSE",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_NURSE_TENANT_LICENSE",
            columnNames = {"TENANT_ID", "NURSE_LICENSE", "IS_DELETED"}),
        @UniqueConstraint(name = "UK_NURSE_IDENTITY_TENANT",
            columnNames = {"FK_ID_PERSON", "TENANT_ID", "IS_DELETED"})
    },
    indexes = {
        @Index(name = "IDX_NURSE_TENANT",    columnList = "TENANT_ID"),
        @Index(name = "IDX_NURSE_PERSON",    columnList = "FK_ID_PERSON"),
        @Index(name = "IDX_NURSE_LICENSE",   columnList = "TENANT_ID, NURSE_LICENSE"),
        @Index(name = "IDX_NURSE_DEPT_UNIT", columnList = "FK_ID_DEPT_UNIT")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Nurse extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_NURSE_PERSON"))
    private Person person;

    // Referencia lógica al usuario del sistema — sin @ManyToOne para no acoplar con core-auth
    @Column(name = "USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DEPT_UNIT",
                foreignKey = @ForeignKey(name = "FK_MED_NURSE_DEPT_UNIT"))
    private DepartmentUnit departmentUnit;

    // Perfil curricular — Nurse es propietario de la FK (OneToOne)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "FK_ID_USERPROFILE",
                foreignKey = @ForeignKey(name = "FK_MED_NURSE_USERPROFILE"))
    private UserProfile userProfile;

    @Column(name = "NURSE_LICENSE", length = 100)
    private String licenseNumber;

    @Column(name = "NURSE_RANK", length = 100)
    private String rank;

    @Enumerated(EnumType.STRING)
    @Column(name = "WORK_SHIFT", length = 50)
    private ShiftEnum workShift;

    @Column(name = "LICENSE_EXPIRY")
    private LocalDate licenseExpiryDate;

    @Column(name = "HIRE_DATE")
    private LocalDate hireDate;

    @Column(name = "CONTRACT_TYPE", length = 50)
    private String contractType;

    @Column(name = "EMERGENCY_CONTACT", length = 200)
    private String emergencyContact;

    @Lob
    @Column(name = "PHOTO", columnDefinition = "LONGBLOB")
    @NotAudited
    private byte[] photo;

    // Habilidades clínicas especializadas (ElementCollection) — no auditado
    @NotAudited
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "MED_NURSE_SKILLS",
        joinColumns = @JoinColumn(name = "ID_NURSE",
                                  foreignKey = @ForeignKey(name = "FK_MED_NURSE_SKILLS_NURSE"))
    )
    @Column(name = "SKILL")
    @Builder.Default
    private Set<String> clinicalSkills = new HashSet<>();

    @PrePersist @PreUpdate
    private void normalize() {
        if (licenseNumber != null) licenseNumber = licenseNumber.trim().toUpperCase();
        if (rank != null) rank = rank.trim().toUpperCase();
    }
}
