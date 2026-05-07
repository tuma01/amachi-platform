package com.amachi.app.vitalia.medicalcore.professional.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import com.amachi.app.core.common.enums.ProfessionalRoleContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Información Profesional")
public class ProfessionalInfoSearchDto extends BaseSearchDto {

    @Schema(description = "ID de la persona", example = "10005")
    private Long personId;

    @Schema(description = "Contexto de rol profesional")
    private ProfessionalRoleContext roleContext;

    @Schema(description = "Filtrar solo posiciones actuales")
    private Boolean isCurrent;

    @Schema(description = "ID de la organización", example = "200")
    private Long organizationId;
}
