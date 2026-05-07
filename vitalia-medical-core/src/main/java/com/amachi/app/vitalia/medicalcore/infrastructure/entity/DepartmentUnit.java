package com.amachi.app.vitalia.medicalcore.infrastructure.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.employee.entity.Employee;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "MED_DEPARTMENT_UNIT",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_DEPT_UNIT_CODE", columnNames = {"TENANT_ID", "CODE"})
    },
    indexes = {
        @Index(name = "IDX_MED_DEPT_UNIT_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_MED_DEPT_UNIT_CODE",   columnList = "TENANT_ID, CODE")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class DepartmentUnit extends AuditableTenantEntity implements Model {

    @Column(name = "NAME", nullable = false, length = 150)
    private String name;

    @Column(name = "CODE", nullable = false, length = 30)
    private String code;

    @Column(name = "FLOOR", length = 20)
    private String floor;

    @Column(name = "CONTACT_PHONE", length = 50)
    private String contactPhone;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "MAX_CAPACITY")
    private Integer maxCapacity;

    @Column(name = "IS_CLINICAL", nullable = false)
    @Builder.Default
    private Boolean isClinical = true;

    @Column(name = "COST_CENTER", length = 50)
    private String costCenter;

    // Jerarquía hospitalaria: una unidad puede contener sub-unidades (ej: Piso 3 → UCI → Box 1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PARENT_UNIT", foreignKey = @ForeignKey(name = "FK_MED_DEPT_UNIT_PARENT"))
    private DepartmentUnit parentUnit;

    @OneToMany(mappedBy = "parentUnit", cascade = CascadeType.ALL)
    @Builder.Default
    private List<DepartmentUnit> subUnits = new ArrayList<>();

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_UNIT_HEAD",
                foreignKey = @ForeignKey(name = "FK_MED_DEPT_UNIT_HEAD"))
    private Employee unitHead;

    // unitType — se activa en fase de integración de catálogos

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (name != null) name = name.trim();
        if (code != null) code = code.trim().toUpperCase();
        if (costCenter != null) costCenter = costCenter.trim().toUpperCase();
    }
}
