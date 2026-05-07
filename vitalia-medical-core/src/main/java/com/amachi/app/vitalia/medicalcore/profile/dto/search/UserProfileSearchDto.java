package com.amachi.app.vitalia.medicalcore.profile.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Schema(description = "Filtros de búsqueda para Perfiles Curriculares")
public class UserProfileSearchDto extends BaseSearchDto {

    @Schema(description = "Filtrar por término en la biografía")
    private String biography;
}
