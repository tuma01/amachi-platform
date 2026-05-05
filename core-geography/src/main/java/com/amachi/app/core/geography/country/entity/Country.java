package com.amachi.app.core.geography.country.entity;

import com.amachi.app.core.common.entity.Auditable;
import com.amachi.app.core.common.entity.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Country Entity (SaaS Elite Tier).
 * Reference model for Global Catalogs, adhering to US English nomenclature.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "GEO_COUNTRY",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_ISO_COUNTRY", columnNames = {"ISO"})
    },
    indexes = {
        @Index(name = "IDX_COUNTRY_ISO", columnList = "ISO"),
        @Index(name = "IDX_COUNTRY_NAME", columnList = "NAME"),
    }
)
public class Country extends Auditable<String> implements Model {

    @NotBlank(message = "ISO code {err.mandatory}")
    @Size(min = 2, max = 2)
    @Column(name = "ISO", nullable = false, length = 2)
    private String iso;

    @NotBlank(message = "Name {err.mandatory}")
    @Size(max = 100)
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "NICE_NAME")
    private String niceName;

    @Size(min = 3, max = 3)
    @Column(name = "ISO3", length = 3)
    private String iso3;

    @Column(name = "NUM_CODE")
    private Integer numCode;

    @NotNull(message = "Phone code {err.mandatory}")
    @Column(name = "PHONE_CODE", nullable = false)
    private Integer phoneCode;

    // ✅ Automatic Normalization (Enterprise Best Practice)
    @PrePersist
    @PreUpdate
    private void normalize() {
        if (iso != null) iso = iso.trim().toUpperCase();
        if (iso3 != null) iso3 = iso3.trim().toUpperCase();
        if (name != null) name = name.trim();
        if (niceName != null) niceName = niceName.trim();
    }
}
