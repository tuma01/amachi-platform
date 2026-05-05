package com.amachi.app.core.domain.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import com.amachi.app.core.common.enums.CivilStatus;
import com.amachi.app.core.common.enums.DocumentType;
import com.amachi.app.core.common.enums.Gender;
import com.amachi.app.core.geography.address.entity.Address;
import org.hibernate.envers.Audited;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Where;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * Entidad Person (SaaS Elite Tier).
 * Base de Identidad Global (Shared Identity Pattern).
 * Esta entidad es Universal y no posee TENANT_ID.
 */
@Entity
@Table(name = "DMN_PERSON")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, exclude = { "personTenants" })
@Audited
@Where(clause = "is_deleted = false")
public class Person extends Auditable<String> implements Model {

    @Column(name = "NATIONAL_ID", length = 100, unique = true)
    private String nationalId;

    @Column(name = "NATIONAL_HEALTH_ID", length = 100, unique = true)
    private String nationalHealthId;

    @Enumerated(EnumType.STRING)
    @Column(name = "DOCUMENT_TYPE", length = 30)
    private DocumentType documentType;

    @NotBlank(message = "{err.required}")
    @Size(min = 2, max = 50)
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "MIDDLE_NAME", length = 50)
    private String middleName;

    @NotBlank(message = "{err.required}")
    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastName;

    @Column(name = "SECOND_LAST_NAME", length = 50)
    private String secondLastName;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "MARITAL_STATUS")
    private CivilStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER")
    private Gender gender;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_ADDRESS", foreignKey = @ForeignKey(name = "FK_PERSON_ADDRESS"))
    private Address address;

    @Column(name = "PHONE_NUMBER", length = 50)
    private String phoneNumber;

    @Column(name = "MOBILE_NUMBER", length = 50)
    private String mobileNumber;

    @Column(name = "EMAIL", length = 100, unique = true)
    private String email;

    @Builder.Default
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonTenant> personTenants = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (this.firstName != null)
            this.firstName = this.firstName.trim();
        if (this.lastName != null)
            this.lastName = this.lastName.trim();
        if (this.nationalId != null)
            this.nationalId = this.nationalId.trim().toUpperCase();
        if (this.nationalHealthId != null)
            this.nationalHealthId = this.nationalHealthId.trim().toUpperCase();
        if (this.email != null)
            this.email = this.email.trim().toLowerCase();
    }

    /**
     * Devuelve el nombre completo (First + Last).
     */
    public String getFullName() {
        return String.format("%s %s", 
            firstName != null ? firstName : "", 
            lastName != null ? lastName : "").trim();
    }
}
