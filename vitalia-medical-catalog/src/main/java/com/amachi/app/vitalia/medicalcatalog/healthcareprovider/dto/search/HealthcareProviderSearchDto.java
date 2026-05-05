package com.amachi.app.vitalia.medicalcatalog.healthcareprovider.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

/**
 * Filtros de búsqueda para Proveedores de Salud (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public final class HealthcareProviderSearchDto extends BaseSearchDto {
    private String query;
}
