package com.amachi.app.core.auth.entity;

import com.amachi.app.core.common.entity.Auditable;
import jakarta.validation.constraints.NotBlank;
import com.amachi.app.core.domain.entity.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Global Identity Entity (Registry Pattern).
 */
@Getter
@SuperBuilder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUT_USER")
public class User extends Auditable<String> implements UserDetails, Principal {

    @Column(name = "IS_DELETED", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    /**
     * SCHEMA COMPATIBILITY FIELDS
     * Logically global, physically scoped in legacy DB.
     */
    @Column(name = "TENANT_ID", nullable = false)
    @Builder.Default
    private Long tenantId = 0L;

    @Column(name = "TENANT_CODE", nullable = false, length = 50)
    @Builder.Default
    private String tenantCode = "GLOBAL";

    @NotBlank(message = "Email is mandatory")
    private String email;
    private boolean enabled;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ACCOUNT_LOCKED", nullable = false)
    private boolean accountLocked;

    @Column(name = "LAST_LOGIN")
    private LocalDateTime lastLogin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_ID_PERSON", foreignKey = @ForeignKey(name = "FK_AUT_USER_PERSON"))
    private Person person;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserAccount> userAccounts = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void normalizeUser() {
        if (this.email != null) {
            this.email = this.email.toLowerCase().trim();
        }
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public String getName() {
        return this.email;
    }
}
