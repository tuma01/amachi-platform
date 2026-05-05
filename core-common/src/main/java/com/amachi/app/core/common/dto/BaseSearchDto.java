package com.amachi.app.core.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Base abstract class for all search DTOs (SaaS Elite Tier).
 * Minimalist architecture to ensure stability and clean compilation.
 */
@Getter
@Setter
@NoArgsConstructor
@lombok.experimental.SuperBuilder(toBuilder = true)
public abstract class BaseSearchDto {
    protected Long id;
}
