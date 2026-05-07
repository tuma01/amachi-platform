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
    name = "MED_USER_EDUCATION",
    indexes = {
        @Index(name = "IDX_USER_EDU_TENANT",  columnList = "TENANT_ID"),
        @Index(name = "IDX_USER_EDU_PROFILE", columnList = "FK_ID_USERPROFILE")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@Audited
public class Education extends AuditableTenantEntity implements Model {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_USERPROFILE", nullable = false,
                foreignKey = @ForeignKey(name = "FK_MED_EDUCATION_PROFILE"))
    private UserProfile profile;

    @Column(name = "INSTITUTION", nullable = false, length = 150)
    private String institution;

    @Column(name = "DEGREE", nullable = false, length = 150)
    private String degree;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @PrePersist @PreUpdate
    private void normalize() {
        if (institution != null) institution = institution.trim();
        if (degree != null) degree = degree.trim();
    }
}
