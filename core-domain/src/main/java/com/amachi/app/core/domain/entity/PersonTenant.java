package com.amachi.app.core.domain.entity;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.entity.SoftDeletable;
import com.amachi.app.core.domain.tenant.entity.Tenant;
import com.amachi.app.core.common.enums.RelationStatus;
import com.amachi.app.core.common.enums.RoleContext;
import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;
import lombok.*;
import org.hibernate.envers.Audited;


import java.time.LocalDateTime;

@Entity
@Table(name = "DMN_PERSON_TENANT",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PERSON_TENANT_ROLE_CONTEXT",
                        columnNames = {"FK_ID_PERSON", "FK_ID_TENANT", "ROLE_CONTEXT", "IS_DELETED"})
        })
@Getter
@SuperBuilder @Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EqualsAndHashCode(callSuper = true)
public class PersonTenant extends AuditableTenantEntity implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_PERSON", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_TENANT", nullable = false)
    private Tenant tenant;

    @Column(name = "NATIONAL_HEALTH_ID", length = 50)
    private String nationalHealthId;

    // ≡ƒö╣ Datos contextuales (ej: doctor en un hospital, paciente en otro)
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_CONTEXT", nullable = false, length = 30)
    private RoleContext roleContext; // DOCTOR, PATIENT, etc.

    @Column(name = "DATE_REGISTERED", nullable = false)
    private LocalDateTime dateRegistered;

    @Column(name = "DATE_UNREGISTERED")
    private LocalDateTime  dateUnregistered;

    @Enumerated(EnumType.STRING)
    @Column(name = "RELATION_STATUS", nullable = false, length = 20)
    private RelationStatus relationStatus; // ACTIVO, INACTIVO, SUSPENDIDO, etc.
}
