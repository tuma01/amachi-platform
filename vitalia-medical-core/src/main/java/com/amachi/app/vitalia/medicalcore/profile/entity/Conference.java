package com.amachi.app.vitalia.medicalcore.profile.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.domain.entity.AuditableTenantEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Table(
    name = "MED_USER_CONFERENCE",
    indexes = {
        @Index(name = "IDX_USER_CONF_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_USER_CONF_PROFILE", columnList = "FK_ID_USERPROFILE")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Conference extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_USERPROFILE", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_CONFERENCE_PROFILE"))
    private UserProfile profile;

    @Column(name = "TOPIC", nullable = false, length = 150)
    private String topic;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "ORGANIZER", length = 150)
    private String organizer;

    @Column(name = "LOCATION", length = 255)
    private String location;

    @Column(name = "IS_INTERNATIONAL", nullable = false)
    @Builder.Default
    private Boolean isInternational = false;

    @Column(name = "CONFERENCE_DATE", nullable = false)
    private LocalDate date;

    @PrePersist @PreUpdate
    private void normalize() {
        if (topic != null) topic = topic.trim();
        if (organizer != null) organizer = organizer.trim();
    }
}
