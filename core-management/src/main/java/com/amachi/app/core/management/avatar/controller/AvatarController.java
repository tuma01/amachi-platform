package com.amachi.app.core.management.avatar.controller;

import com.amachi.app.core.common.exception.ResourceNotFoundException;
import com.amachi.app.core.management.avatar.dto.AvatarDto;
import com.amachi.app.core.management.avatar.entity.Avatar;
import com.amachi.app.core.management.avatar.mapper.AvatarMapper;
import com.amachi.app.core.management.avatar.service.AvatarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AvatarController implements AvatarApi {

    private final AvatarService avatarService;
    private final AvatarMapper avatarMapper;

    @Override
    public ResponseEntity<AvatarDto> getAvatarMetadata(Long userId) {
        log.info("📷 [GET /avatars/user/{}] Fetching avatar metadata", userId);
        Avatar avatar = avatarService.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", "error.resource.not.found", userId));
        return ResponseEntity.ok(avatarMapper.toDto(avatar));
    }

    @Override
    public ResponseEntity<byte[]> getAvatarContent(Long userId) {
        log.info("📷 [GET /avatars/user/{}/content] Downloading avatar", userId);
        Avatar avatar = avatarService.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Avatar", "error.resource.not.found", userId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                avatar.getMimeType() != null ? avatar.getMimeType() : MediaType.APPLICATION_OCTET_STREAM_VALUE));
        headers.setContentDispositionFormData("inline", avatar.getFilename());

        return ResponseEntity.ok().headers(headers).body(avatar.getContent());
    }

    @Override
    public ResponseEntity<AvatarDto> uploadAvatar(Long userId, MultipartFile file) {
        log.info("📷 [POST /avatars/user/{}] Uploading avatar, size={}B", userId, file.getSize());
        try {
            Avatar saved = avatarService.upload(userId, file);
            return ResponseEntity.ok(avatarMapper.toDto(saved));
        } catch (IOException e) {
            log.error("Failed to read avatar file for userId={}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteAvatar(Long userId) {
        log.info("🗑️ [DELETE /avatars/user/{}] Deleting avatar", userId);
        avatarService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
