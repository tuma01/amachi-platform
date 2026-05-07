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
    name = "MED_USER_COURSE",
    indexes = {
        @Index(name = "IDX_USER_CRS_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_USER_CRS_PROFILE", columnList = "FK_ID_USERPROFILE")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Course extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_USERPROFILE", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_COURSE_PROFILE"))
    private UserProfile profile;

    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    @Column(name = "INSTITUTION", nullable = false, length = 150)
    private String institution;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "COURSE_DATE", nullable = false)
    private LocalDate courseDate;

    @Column(name = "DURATION_HOURS", nullable = false)
    private Integer durationInHours;

    @Column(name = "CERTIFICATE_REF", length = 255)
    private String certificate;

    @PrePersist @PreUpdate
    private void normalize() {
        if (title != null) title = title.trim();
        if (institution != null) institution = institution.trim();
    }
}
