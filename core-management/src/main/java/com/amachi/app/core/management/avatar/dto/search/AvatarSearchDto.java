package com.amachi.app.core.management.avatar.dto.search;

import com.amachi.app.core.common.dto.BaseSearchDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarSearchDto extends BaseSearchDto {
    private Long userId;
    private String mimeType;
}
