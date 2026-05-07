package com.amachi.app.vitalia.medicalcore.organization.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrganizationSearchDto extends BaseSearchDto {
    private String name;
    private String type;
    private Boolean active;
}
