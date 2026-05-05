package com.amachi.app.vitalia.medicalcatalog.medication.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

/**
 * Filtros de búsqueda para Medicamentos (SaaS Elite Tier).
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Hidden
public final class MedicationSearchDto extends BaseSearchDto {
    private String query;
    private String code;
}
