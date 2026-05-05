package com.amachi.app.core.management.avatar.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Avatar", description = "Metadata del avatar de usuario. El contenido binario se sirve en un endpoint separado.")
public class AvatarDto {

    @Schema(description = "ID del avatar", example = "1")
    private Long id;

    @Schema(description = "Nombre del archivo original", example = "profile.png")
    private String filename;

    @Schema(description = "MIME type del archivo", example = "image/png")
    private String mimeType;

    @Schema(description = "Tamaño en bytes", example = "204800")
    private Long size;

    @Schema(description = "ID del usuario propietario", example = "42")
    private Long userId;
}
