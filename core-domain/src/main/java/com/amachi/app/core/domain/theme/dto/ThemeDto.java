package com.amachi.app.core.domain.theme.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.amachi.app.core.common.enums.ThemeMode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {
    private Long id;
    private String code;
    private String name;
    private String logoUrl;
    private String faviconUrl;
    private String primaryColor;
    private String secondaryColor;
    private String backgroundColor;
    private String textColor;
    private String accentColor;
    private String warnColor;
    private String linkColor;
    private String buttonTextColor;
    private String fontFamily;
    private ThemeMode themeMode;
    private String propertiesJson;
    private String customCss;
    private boolean allowCustomCss;
    private boolean active;
    private boolean template;
    private Integer version;
}
