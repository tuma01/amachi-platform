package com.amachi.app.core.common.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> extends BaseEntity {

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false, length = 100)
    private U createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", length = 100)
    private U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private LocalDateTime lastModifiedDate;

    // Optimistic Locking (Resiliencia Médica)
    @Version
    @Column(name = "VERSION")
    private Long version;

    // ID Externo para APIs públicas e Idempotencia (Seguridad Tier-1)
    @Column(name = "EXTERNAL_ID", unique = true, updatable = false, length = 36)
    private String externalId;

    @PrePersist
    protected void onPrePersistCommon() {
        if (this.externalId == null) {
            this.externalId = java.util.UUID.randomUUID().toString();
        }
    }
}
