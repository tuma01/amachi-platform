package com.amachi.app.core.management.avatar.controller;

import com.amachi.app.core.management.avatar.dto.AvatarDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Avatar", description = "Gestión de avatares de usuario por tenant")
@RequestMapping("/avatars")
public interface AvatarApi {

    @Operation(
        summary = "Obtener metadata del avatar",
        description = "Devuelve la metadata del avatar del usuario en el tenant actual.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Metadata del avatar."),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado.")
        }
    )
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AvatarDto> getAvatarMetadata(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("userId") Long userId
    );

    @Operation(
        summary = "Descargar imagen del avatar",
        description = "Devuelve el contenido binario del avatar. El Content-Type refleja el MIME del archivo.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Imagen del avatar."),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado.")
        }
    )
    @GetMapping(value = "/user/{userId}/content")
    ResponseEntity<byte[]> getAvatarContent(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("userId") Long userId
    );

    @Operation(
        summary = "Subir o reemplazar avatar",
        description = "Sube una imagen como avatar del usuario. Si ya existe, lo reemplaza.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Avatar guardado."),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o vacío.")
        }
    )
    @PostMapping(value = "/user/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                 produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AvatarDto> uploadAvatar(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("userId") Long userId,
            @Parameter(description = "Archivo de imagen (PNG, JPG, WEBP)", required = true)
            @RequestPart("file") MultipartFile file
    );

    @Operation(
        summary = "Eliminar avatar",
        description = "Elimina el avatar del usuario en el tenant actual.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Avatar eliminado."),
            @ApiResponse(responseCode = "404", description = "Avatar no encontrado.")
        }
    )
    @DeleteMapping(value = "/user/{userId}")
    ResponseEntity<Void> deleteAvatar(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable("userId") Long userId
    );
}
