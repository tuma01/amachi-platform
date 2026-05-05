package com.amachi.app.core.auth.auditevent.entity;

import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.common.enums.AuditEventType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "AUT_AUDIT_EVENTS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEvent implements SoftDeletable, Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TENANT_CODE", nullable = false, length = 50)
    private String tenantCode;

    @Column(name = "TENANT_ID", nullable = false)
    private Long tenantId;

    @Column(name = "EXTERNAL_ID", nullable = false, unique = true, length = 36)
    @Builder.Default
    private String externalId = java.util.UUID.randomUUID().toString();

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "EVENT_TYPE", nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private AuditEventType eventType;

    @Column(name = "FK_ID_USER", nullable = false)
    private Long userId;

    @Column(name = "MESSAGE", nullable = false, length = 500)
    private String message;

    @Column(name = "TIMESTAMP", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "IP_ADDRESS", nullable = true)
    private String ipAddress;

    @PrePersist
    protected void onPrePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    @Override
    public void delete() {
        this.deleted = true;
    }

    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
