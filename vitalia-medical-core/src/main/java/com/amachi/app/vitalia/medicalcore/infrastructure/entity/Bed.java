package com.amachi.app.vitalia.medicalcore.infrastructure.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.BedStatusEnum;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(
    name = "MED_BED",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_BED_CODE_ROOM", columnNames = {"FK_ID_ROOM", "BED_CODE"})
    },
    indexes = {
        @Index(name = "IDX_MED_BED_TENANT", columnList = "TENANT_ID"),
        @Index(name = "IDX_MED_BED_ROOM",   columnList = "FK_ID_ROOM"),
        @Index(name = "IDX_MED_BED_STATUS", columnList = "STATUS")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Bed extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_ROOM", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_BED_ROOM"))
    private Room room;

    @Column(name = "BED_NUMBER", nullable = false, length = 20)
    private String bedNumber;

    @Column(name = "BED_CODE", nullable = false, length = 30)
    private String bedCode;

    @Column(name = "IS_OCCUPIED", nullable = false)
    @Builder.Default
    private Boolean isOccupied = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    @Builder.Default
    private BedStatusEnum status = BedStatusEnum.AVAILABLE;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "MAINTENANCE_DUE")
    private LocalDate maintenanceDue;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (bedNumber != null) bedNumber = bedNumber.trim().toUpperCase();
        if (bedCode != null) bedCode = bedCode.trim().toUpperCase();
    }
}
