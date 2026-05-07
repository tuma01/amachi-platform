package com.amachi.app.vitalia.medicalcore.infrastructure.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.RoomTypeEnum;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import com.amachi.app.vitalia.medicalcore.common.enums.CleaningStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "MED_ROOM",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_MED_ROOM_NUMBER_UNIT", columnNames = {"FK_ID_DEPT_UNIT", "ROOM_NUMBER"})
    },
    indexes = {
        @Index(name = "IDX_MED_ROOM_TENANT",   columnList = "TENANT_ID"),
        @Index(name = "IDX_MED_ROOM_UNIT",     columnList = "FK_ID_DEPT_UNIT"),
        @Index(name = "IDX_MED_ROOM_CLEANING", columnList = "CLEANING_STATUS")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Room extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_DEPT_UNIT", nullable = false, foreignKey = @ForeignKey(name = "FK_MED_ROOM_DEPT_UNIT"))
    private DepartmentUnit unit;

    @Column(name = "ROOM_NUMBER", nullable = false, length = 20)
    private String roomNumber;

    @Column(name = "IS_PRIVATE")
    @Builder.Default
    private Boolean privateRoom = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROOM_TYPE", length = 30)
    private RoomTypeEnum roomType;

    @Column(name = "BLOCK_FLOOR")
    private Integer blockFloor;

    @Column(name = "BLOCK_CODE", length = 20)
    private String blockCode;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLEANING_STATUS", length = 30)
    @Builder.Default
    private CleaningStatus cleaningStatus = CleaningStatus.CLEAN;

    @Column(name = "IS_ACTIVE", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Bed> beds = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (roomNumber != null) roomNumber = roomNumber.trim().toUpperCase();
        if (blockCode != null) blockCode = blockCode.trim().toUpperCase();
    }
}
